---------------------------------
---第一部分 注释:网元命令详细----
---------------------------------
--@name=
--@desc=ZNEL;
--@type=MSC
--@vendor=NOKIA
--@version=
--@script=lua
--@author=
--@date=
---------------------------------
-----第二部分 描述配置表---------
---------------------------------
require "nsn_common"
--返回参数顺序---------------------------------------
config={}
--是否入库:1表示入库;0表示不入库---------------------1
config.isInputDB ="0"
--是否写SQL:1表示写;0表示不写;2表示load方法------------------------2
config.isWriteSQL ="2"
--数据源名称-----------------------------------------3
config.dataSource = "dhlr_nsn"
--数据库名称-----------------------------------------4
config.dataBase = "dhlr"
--数据表名称-----------------------------------------5 不写SQL时有效
config.table = {}
config.table.tb ={
--表1名
"provgw_log"
}
--字段名，按照表字段顺序
config.table.row={
	{
	--'isdn',
	'request_id',
	--'rs_time_ms',
	'isdn',
	'identifier',
	'unit',
	'instance',
	'operation_user',
	'operation',
	'execution_result',
	'error_code',
	'error_message',
	'response_time',
	'perform_content'
	--'result'
	}
}
--SQL语句-------------------------------------------7
config.table.sql = {
--表1SQL
'insert into des.mss_hlr_his_alarm(ne_time,server,instance,user,ne_os,id,result,error_code,error_message,Perform_content) values(?,?,?,?,?,?,?,?,?,?)'
}
config.endRegs={"COMMAND EXECUTED"}
-----------------------------------
------第三部分 命令完整性正则------
-----------------------------------
config.type = {}
--匹配记录正则
config.type.regs={
}
--------------------------------
------第四部分 解析方法---------
--------------------------------
------正则解析程序开始----------
----解析日志方法----------------
function analyseLogs(config,extra,cmdResult)
	--local _str = cmdResult
	local result = ""   -----存放字符串结果
	local result_temp=''
	local allResultSet={}
	local table1Data={}
	local s=""
	local s1=''
	local s2=''
	local s0=''
	local ne_time='' local server='' local instance='' local user=''  local identifier='' local isdn='' ne_time1=''
	local ne_os=''  local id='' local result='' local error_code='' local error_message='' local Perform_content=''
    ---local a1=''
	------解析字段------------
	--获取真实报文路径  先注释掉
	--for line  in string.gmatch(cmdResult,"[^\n]*\n") do
	--provgw-spml_command.log.2015_04_21-16_29_06_3 100% 1452KB   1.4MB/s   00:00
	-- s11=string.match(line,'(.-)%s+%d+.+')
	 -- if s11~=nil then
	 --   s0=s11
	-- end
	--end
	--s2='/home/jcomp/jrdas/JWE/add_log/report/'..s0
	--'/home/jcomp/jwe/nsncoAdapter/report/'
	--local cmdResult1 = getMsg(s2)
    --
	local cmdResult= string.gsub(cmdResult,'@','')
	--把散行整合为一行
	local ss= string.gsub(cmdResult,'\n','')
	   -- print(ss)
	local s00=string.gsub(ss,'</spml:.->','</spml:a>\n')
	ss=nil --清空
	--print('s00 /n....................done')
	local str_cache=''  --
	local tableaa1={} --请求  aa
	local tablebb1={} --响应  bb
	for line  in string.gmatch(s00,"[^\n]*\n") do
	   if string.match(line,'^%d+%-%d+%-%d+.+<spml:.*Response.+>') then
	     --print(line)
	     --s1=string.match(line,'.+<spml:.*Response.+>')
		 table.insert(tablebb1,line)
		elseif string.match(line,'^%d+%-%d+%-%d+.+<spml:.*Request.+>') then
		--print(line)
		   -- s=string.match(line ,'.+<spml:.*Request%s*.+>')
			str_cache=str_cache..line
			table.insert(tableaa1,line)
	   else
	   end
	end
	 s00=nil   --清空
	-- local str_Result = getMsg('/home/shh_dhss/jwe_dataloader/boss_soap/boss_pgw_'..extra[2])
	-- if str_Result~='' or str_Result~=nil then
	--   for line in string.gmatch(str_Result,"[^\n]*\n") do
	 --    table.insert(tableaa1,line)
--
	--   end
	-- end
	-- str_Result=nil
	--local FILE_WRITE1='/home/shh_dhss/jwe_dataloader/boss_soap/boss_pgw_'..extra[2]
	--local f = assert(io.open(FILE_WRITE1, 'w'))
	--	   f:write(str_cache)
	--	   f:close()
   -- str_cache=nil
	-- print('tableaa1 and tablebb1 sort....................done')
    -- local file = io.open("d:\111\out.txt", "w")
	-- assert(file)
   --对整合后的内容 进行解析
   local tableaa={}
   local sa1=''
   local sa2=''
    for k,line in pairs(tableaa1) do
	--<spml:modifyRequest requestID="-679e8650:149cbcfb562:6c51"
	    sa1=string.match(line,'.+(<spml.+>)\n*')
		--sa2=string.match(sa1,'.+requestID=\"(.+)\".+')
		--print(sa1)
		table.insert(tableaa,sa1)
	end
	tableaa1=nil  --清空
	-- print('tableaa_insert....................done')
	    --for k,line in pairs(tablebb1) do
		local str1=''
	   for i=#tablebb1,1,-1 do
			line = tablebb1[i]
			--print(line)
			--result="success"
		 if line then
			s=string.match(line,'(.+result=%"success%".+)')
			--s1=string.match(line,'(.+result=%"failure%".+)')
			id=string.match(line,'.+requestID=%"(.-)%"%s*.+')
			if s then
			   --2014-11-20 09:27:18,420 zzn01pg01 instance1 soapUser DEFAULT
			   ne_time1,server,instance,user,ne_os,result=string.match(s,'%s*(%d+-%d+-%d+%s+%d+:%d+:%d+,%d+)%s+(.+)%s+(.+)%s+(%a+)%s+(%a+)%s+(<.+)')
			   ne_time=string.match(ne_time1,'(%d+-%d+-%d+%s+%d+:%d+:%d+),%d+')
			   for i=#tableaa,1,-1 do
			   ---679e8650:149c85fda00:-861
			      sa2=string.match(tableaa[i],'.+requestID=%"(.-)%"%s+.+')
				  --print(sa2)
				  if sa2==id then
					 --print(tableaa[i])
					 str1=string.match(tableaa[i],'<spml:(.-)%s+.+')
					-- print(str1)
			         tableaa[i]=string.gsub(tableaa[i],'</spml:a>','</spml:'..str1..'>')
					 Perform_content=tableaa[i]
					-- print(Perform_content)
					 table.remove(tableaa,i)
					 break
				  end
			   end
			   -- print(trim(Perform_content))
                --<identifier>460024590500022</identifier>
               identifier=string.match(Perform_content,'.+<.*identifier.*>(.-)</identifier>.+')
			   if identifier~=nil  and string.match(identifier,'^86%d+') then
                    isdn= identifier
					identifier=''
			   end
			   if identifier=='' or identifier==nil then  --<imsi>460024399409631</imsi>
			      identifier=string.match(Perform_content,'.+<imsi>(.-)</imsi>.+')
			   end
			   if identifier=='' or identifier==nil then  --<imsi>460024399409631</imsi>
			      identifier=string.match(Perform_content,'.+>(460.-)<.+')
			   end
			   if  identifier==nil  or identifier=='' then
			              identifier='' end
				if isdn=='' and isdn==nil then --<msisdn>8618439949362</msisdn>
				  isdn=string.match(Perform_content,'.+<msisdn>(.-)</msisdn>.+')
				end
				if isdn==nil then isdn='' end
			   --<msisdn>8618439940101</msisdn>
			  -- isdn=string.match(Perform_content,'.+<%a*isdn>(.-)</%a*isdn>.+')
			   --if isdn==nil then isdn='' end
			   error_code=''
			   error_message=''   --trim(ne_time) trim(id)
			   ---s2=''
			  -- s2=trim(id)..'@'..ne_time1..'@'..isdn..'@'..identifier..'@'..server..'@'..instance..'@'..user..'@'..ne_os..'@'..'success'..'@'..trim(error_code)..'@'..trim(error_message)..'@'..trim(ne_time)..'@'..trim(Perform_content)..';;;'
			   s2=trim(id)..'@'..isdn..'@'..identifier..'@'..server..'@'..instance..'@'..user..'@'..ne_os..'@'..'success'..'@'..trim(error_code)..'@'..trim(error_message)..'@'..trim(ne_time)..'@'..trim(Perform_content)..';;;'
			   table.insert(table1Data,s2)
			else
			  --2014-11-20 11:54:23,700 zzn01pg01 instance1 soapUser DEFAULT <spml:modifyResponse  </spml:modifyResponse>
			  ne_time1,server,instance,user,ne_os,result=string.match(line,'%s*(%d+-%d+-%d+%s+%d+:%d+:%d+,%d+)%s+(.-)%s+(.-)%s+(.-)%s+(.-)%s+(<spml:.+</spml:.->)')
              ne_time=string.match(ne_time1,'(%d+-%d+-%d+%s+%d+:%d+:%d+),%d+')
                           --                '.+errorCode=%"(.-)%"%s+.+'
			  error_code=string.match(result,'.+errorCode=%"(.-)%"%s+.+')
			  --<errorMessage>Object msisdn=8618439945248,dc=MSISDN,dc=C-NTDB already exists.</errorMessage>
			 -- print('333')
              error_message=string.match(result,'.+(<errorMessage>.+</errorMessage>).+')
			 -- print(identifier)
			  ---print('444')
			  --identifier=string.match(result,'.+<identifier%s*.*>(.-)</identifier>.+')
			 -- ne_time=string.gsub(ne_time,'%X','')
			 for i=#tableaa,1,-1 do
			   ---679e8650:149c85fda00:-861
			      sa2=string.match(tableaa[i],'.+requestID=%"(.-)%"%s+.+')
				  --print(sa2)
				  if sa2==id then
				    -- print('sa21'..sa2)
					 str1=string.match(tableaa[i],'<spml:(.-)%s+.+')
			         tableaa[i]=string.gsub(tableaa[i],'</spml:a>','</spml:'..str1..'>')
					 Perform_content=tableaa[i]
					 table.remove(tableaa,i)
					 --print(Perform_content)
					 break
				  end
			   end
                identifier=string.match(Perform_content,'.+<identifier.*>(.-)</identifier>.+')
			   if identifier~=nil  and string.match(identifier,'^86%d+') then
                    isdn= identifier
					identifier=''
			   end
			   if identifier=='' or identifier==nil then  --<imsi>460024399409631</imsi>
			      identifier=string.match(Perform_content,'.+<imsi>(.-)</imsi>.+')
			   end
			   if identifier=='' or identifier==nil then  --<imsi>460024399409631</imsi>
			      identifier=string.match(Perform_content,'.+>(460.-)<.+')
			   end
			   if  identifier==nil  or identifier=='' then
			              identifier='' end
				if isdn=='' and isdn==nil then --<msisdn>8618439949362</msisdn>
				  isdn=string.match(Perform_content,'.+<msisdn>(.-)</msisdn>.+')
				end
				if isdn==nil then isdn='' end
			  -- isdn=string.match(Perform_content,'.+<%a*isdn>(.-)</%a*isdn>.+')
              -- if isdn==nil then isdn='' end
			   s2=trim(id)..'@'..isdn..'@'..identifier..'@'..server..'@'..instance..'@'..user..'@'..ne_os..'@'..'failure'..'@'..trim(error_code)..'@'..trim(error_message)..'@'..ne_time..'@'..trim(Perform_content)..';;;'
			   --s2=trim(id)..'@'..trim(identifier)..'@'..trim(server)..'@'..trim(instance)..'@'..trim(user)..'@'..trim(ne_os)..'@'..'success'..'@'..trim(error_code)..'@'..trim(error_message)..'@'..trim(ne_time)..'@'..trim(Perform_content)..';;;'
			  table.insert(table1Data,s2)
			end
		 end
	    end
    table.insert(allResultSet,table1Data)
   ---  file:close()
	return createResultWithTab(config,allResultSet)
end
---------------------------
---第六部分 JAVA调用接口---
---------------------------
--参数1 时间戳 _datetime
--参数2 网元名 _nename
--参数3 文件路径 file
--返回值 表名；解析报文
function jmain(_datetime,_nename,_file)
	--读取原始报文文件
	-- /home/jcomp/jrdas/JWE/add_log/report/provgw-spml_command.log.2015_04_21-16_29_06_3:
	--_file=_nename..'.src'
	--_file='/home/shh_dhss/adapter_soap_pgw_log/soap_log/'.._file
    _file=_datetime..'-'.._nename..'.src'
	_file='/home/jcomp/jrdas/JWE/add_log/report/'.._file
	--_file='/home/jcomp/jrdas/JWE/add_log/report/'.._file
    local _cmdResult = getMsg(_file)
	--print(_cmdResult)
	--local _datetime=string.gsub(_datetime,'%X','')
	--解析原始报文
	result = analyseLogs(config,{_datetime,_nename},_cmdResult)
	--返回表名和解析报文
	return result
end
--测试方法，发布时要注释掉--
function test()
   -- local t1=os.time()
	-- t1=os.date("%x",t1)
   --  print(t1)
	  cmdResult_ = jmain("2014-12-05","OneNDS","11.txt") --provgw-spml_command.log.2014_11_20-09_27_18_347   1111.txt
      local FILE_WRITE1='d:/111/778.txt'
	  local f = assert(io.open(FILE_WRITE1, 'w'))
	  f:write(cmdResult_,'\n')
	  f:close()
	--print("记录:"..cmdResult_)
	--local t2=os.time()
	-- t2=os.date("%x",t2)
	--- print(t2)
	--print(t2-t1)
end
--test()

----------------------------------------------------------------------------------
--The following code is added by the JWEngine, current time: 2015-09-21 21:00:24
----------------------------------------------------------------------------------
if table.getn(arg)==3 then
 	local p1 = arg[1]
	local p2 = arg[2]
	local p3 = arg[3]

	local scritpResultStr = jmain(p1,p2,p3)
	print(scritpResultStr)
else
	local p=""
	for i=1,table.getn(arg) do
		p = p..arg[i].."|"
	end
	p = string.sub(p,0,-2)
	error('Scripte error:not enought parameter,requires 3 parameters,Known parameters:['..p..']')
end
---------------------------------------------------------------------------------

