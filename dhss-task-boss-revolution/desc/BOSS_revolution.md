#  **BOSS业务数据处理功能Review文档（移动版）**

## 重写目的
* 简化程序，用框架来减少代码量，便于测试和维护。
* 将BOSS业务数据监控、回溯、数据优化整合起来。
* 提高数据采集和解析的性能。   
* 提高查询效率。

## 运行环境
* JDK 1.8或更高版本
* MySQL 5.6或更高版本
* 2T或以上的磁盘空间
## 需求描述
* 在页面上展示BOSS业务的实时数据，并按照多个时间粒度（15分钟，小时、天、月等）计算出各种业务类型的成功率。

## BOSS业务数据流模式
1. 在每个SOAP-GW上的 /var/log/NPM 路径下存放BOSS业务数据以及其他与BOSS业务数据无关的日志和数据文件。
2. BOSS业务数据分两大类，第一类是文件名以“BOSS_SOAP_Agent_BOSSA_main_”开头的文件。
例如“BOSS_SOAP_Agent_BOSSA_main_2016-10-17.00-00-02-639”。含义是2016年10点17分00点00分02秒639毫秒开始统计的下发指令的日志。

  日志格式：
          2016-10-16 23:59:56 955|User: boss1| id:a-e23770d7-2fa9-44fe-b9d3-1a62c1eb059e#1476633596955 |{"HLRSN":"52","IMSI":"460026217334274","PRIO":"0","HLRID":"BE52","operationName":"RMV_IMSSUB","FTP_NEID":"","LDAP_NEID":"","FTN_INDEX":"","OPERATION":"RMV_IMSSUB_SPML"}|
          2016-10-16 23:59:56 981|User: boss1| id:a-e23770d7-2fa9-44fe-b9d3-1a62c1eb059e#1476633596955 |Callback successful|task result successful
  特点是每条请求ID（re_id）对应两条数据，一条是发送的请求，一条是成功回执。这个成功指的是数据传送的成功，不是指业务上的成功。

  当“BOSS_SOAP_Agent_BOSSA_main_”这类文件写满约80MB之后，会自动打包成gz压缩包，然后放到/var/log/NPM/backup目录下。名称只是多了一个".gz"
  也就是说，始终只有一个文件是在持续的写入数据的，写满80MB后压缩打包，放在./backup下保存。
3. 第二类BOSS日志文件是业务失败类数据，也保存在/var/log/NPM下，名称"BOSS_ERR_CASE.log"。这个文件是持续写入最新的数据的，当写满约10MB左右后，重命名并压缩放在./backup路径下。重命名的规则：在原有的名字后加上时间戳和序号，如“BOSS_ERR_CASE.log.2016-10-01.1.gz”。表示2016年10月1号的第一个压缩文件。

    日志格式：

            2016-10-01 09:53:06,598 : task id : a-81c23a50-8ef7-4efd-8fb8-9b56e6caee29#1475286786578
        =========================================================
        request:
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:hss="http://www.chinamobile.com/HSS/">
           <soapenv:Header>
              <hss:PassWord>565630f530a957ebdcb04967b625546973b54510</hss:PassWord>
              <hss:UserName>boss1</hss:UserName>
           </soapenv:Header>
           <soapenv:Body>
              <hss:RMV_EPSSUB>
                 <hss:HLRSN>50</hss:HLRSN>
                 <!--Optional:-->
                 <hss:IMSI>460028004328863</hss:IMSI>
              </hss:RMV_EPSSUB>
           </soapenv:Body>
        </soapenv:Envelope>

        response:
        <SOAP-ENV:Envelope  xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
        <SOAP-ENV:Body>
            <RMV_EPSSUBResponse xmlns="http://www.chinamobile.com/HSS/">
                <Result>
                    <ResultCode>3001</ResultCode>
                    <ResultDesc>the EPS subscriber doesn't exist</ResultDesc>
                </Result>
            </RMV_EPSSUBResponse>
        </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
4. 数据内容解释：根据经验来看，BOSS_SOAP_Agent_BOSSA_main_开头的这类文件里面保存的都是各种下发指令的日志。每条请求ID对应两条日志，请求ID是唯一的。第一条日志的信息以JSON的形式提供这条指令下发的详细信息。第二条日志是指令执行的状态。这个指令执行状态一般情况下都是成功的（Callback successful|task result successful），只有在极为罕见的情况下才会失败。并且指令下发成功失败和业务成功失败是没有关系的。BOSS_ERR_CASE.log记录了业务失败的日志。业务失败类型分为2种。第一种，暂时称之为普通业务失败数据。这种数据的请求ID是可以在BOSS_SOAP_Agent_BOSSA_main文件中找到的。也就是说，BOSS_SOAP_Agent_BOSSA_main中，下发的指令业务上失败的话，会在BOSS_ERR_CASE.log中记录。第二种是HeartBeat失败日志，这种失败的请求ID在BOSS_SOAP_Agent_BOSSA_main 查找不到。目前为止含义不明，需要了解一下。老版本的BOSS任务是把上述所有的数据全都插入库里。

## 新版BOSS程序设计
特性：
* 基于Spring Boot的定时任务框架。
* 基于Linux的rsync文件同步命令。
* 实现手动和自动回溯数据功能。
* 更加快速的查询。
* 更加稳定的数据结构和封装的、健壮的解析方法。

流程：
