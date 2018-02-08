
/*	调用方法: $("dom").DataList(options);
	that: dom对象
	options={
		dataSource:[{id:"",value:""}],
		ajax:{
			queryUrl:"",
		//	addUrl:
		//	editUrl:
	//		removeUrl:
		}
	}
*/
;(function($) {
	
//	initCondition();

    var DataList = function($that, options) {

        var defaults = {
            name: "我的自定义插件",
            version: "1.0"
        };
        var options = (typeof(options) == 'object') ? options : {};

        this.dom = $that;
        this.options = $.extend(defaults, options);
        if (!this.options.dataSource || !(this.options.dataSource instanceof Array)) {
            this.options.dataSource = [];
        }
    };
    DataList.prototype.queryAjax = function(para) {
            $.ajax({
                dataType: 'json',
                type: "POST",
                "url": this.options.ajax.queryUrl,
                "data": JSON.stringify(para),
                success: function(data) {
                	if(!(data instanceof Array)){
                		data=[];
                	}
                    initHtml(data);
                },
                fail: function(e) {
                    layer.msg('查询数据失败！', { icon: 1 });
                }
            });
        },
        DataList.prototype.addAjax = function(para) {
            var that = this;
            $.ajax({
                dataType: 'json',
                type: "POST",
                "url": this.options.ajax.addUrl,
                "data": JSON.stringify(para),
                success: function(data) {
                    layer.msg('保存成功', { icon: 2 });
                },
                fail: function(e) {
                    layer.msg('保存失败！', { icon: 1 });
                }
            });
        },
        DataList.prototype.removeAjax = function(ids) {
            var that = this;
            $.ajax({
                dataType: 'json',
                type: "post",
                url: this.options.ajax.removeUrl + ids,
                contentType: "application/json;charset=UTF-8",
                success: function(data) {
                    layer.msg('删除成功', { icon: 2 });
                },
                fail: function(e) {
                    layer.msg('删除失败！', { icon: 1 });
                }
            });
        },
        DataList.prototype.editAjax = function(para) {
            var that = this;
            $.ajax({
                dataType: 'json',
                type: "POST",
                "url": this.options.ajax.addUrl,
                "data": JSON.stringify(para),
                success: function(data) {
                    layer.msg('修改成功', { icon: 2 });
                },
                fail: function(e) {
                    layer.msg('修改失败！', { icon: 1 });
                }
            });
        },
        //编辑框html
        DataList.prototype.getEditFormHtml = function(editing) {
            var html = '<div class="editForm textInput clearfix">' +
                '<input type="text" >' +
                '<button type="button" class="saveTextBtn" title="确认"><img src="assets/img/ok.png" /></button>' +
                '<button type="button" class="cancelTextBtn" title="取消"><img src="assets/img/cancel.png" /></button>';
            if (editing) {
                html += '<button type="button" class="returnTextBtn"  title="原值"><img src="assets/img/return.png" /></button>';
            }
            html += '</div>';
            return html;
        },

        //创建对象内容
        DataList.prototype.init = function() {
            var that = this;
            var dataSource = that.options.dataSource;
            if (that.options.ajax && that.options.ajax.queryUrl.length > 0) {
                that.queryAjax();
            } else if(dataSource) {
            	that.initHtml(dataSource);
            }

            this.$current = null; //当前编辑对象

            this.edit();
            this.cancelEdit();
            this.saveEdit();
            this.returnText();
            this.remove();
            this.removeMulti();
            this.saveChanged();
            this.prevAdd();
            this.nextAdd();
        };

    DataList.prototype.initHtml = function(dataSource) {
    	var that=this;
        that.dom.addClass('textList');
        var html = '';
        $.each(dataSource, function(index, dateItem) {
            if (!dateItem.id) {
                dateItem.id = '';
            }
            html += that.dataItemHtml(dateItem.id, dateItem.value);
        });
        html += '<div class="optionsForm">' +
            ' <button type="button" class="removeCheckedBtn btn btn-xs btn-default">批量删除</button>' +
            ' <button type="button" class="saveChangedBtn btn btn-xs btn-danger">更新数据</button>' +
            '</div>';
        that.dom.html(html);
    };

    //ajax 单个删除按钮，根据id判断删除的是数据库中数据还是没有保存到数据库中的数据
    DataList.prototype.remove = function() {
        var that = this;
        this.dom.delegate('.deleteTextBtn', "click", function() {
            var dataItem = $(this).closest('.dataItem');
            layer.confirm('确认删除么？', {
                btn: ['确认', '取消'] //按钮
            }, function(index) {
                var id = dataItem.attr("data-id");
                if (id.length > 0) { //数据库中数据
                    //调用封装好的 删除ajax方法
                    if (that.options.ajax && that.options.ajax.removeUrl) {
                        that.removeAjax(id);
                    }

                } else { //本地数据
                    dataItem.remove();
                    layer.msg('删除成功', { icon: 1 });
                }
                layer.close(index);
            }, function() {});
        });
    };
    //ajax 单个删除按钮，根据id判断删除的是数据库中数据还是没有保存到数据库中的数据
    DataList.prototype.removeMulti = function() {
        var that = this;
        this.dom.delegate('.removeCheckedBtn', "click", function() {
            var checked = that.dom.find("input[type='checkbox']:checked");
            var dbId = [],
                $dataItem, id;

            if (checked.length > 0) {
                layer.confirm('确认删除么？', {
                    btn: ['确认', '取消'] //按钮
                }, function(index) {
                    $.each(checked, function(index, dataItem) {
                        $dataItem = $(dataItem).closest('.dataItem');
                        id = $dataItem.attr("data-id");
                        if (id.length > 0) {
                            //是数据库中数据
                            dbId.push(id);
                        } else { //本地数据
                            $dataItem.remove();
                        }
                    });

                    if (dbId.length > 0 && that.options.ajax && that.options.ajax.removeUrl) {
                        //remove Ajax
                        that.removeAjax(dbId.slice(","));
                    } else {
                        layer.msg('删除成功！', { icon: 1 });
                    }
                    layer.close(index);
                }, function() {});
            } else {
                layer.msg('请选择删除项目', { icon: 2 });
            }

        });
    };
    //ajax 批量 保存数据
    DataList.prototype.saveChanged = function() {
        var that = this;
        that.dom.delegate(".saveChangedBtn ", "click", function() {
            var dbChangedData = []; //已修改数据
            var newData = []; //新添加数据
            var $changedData = that.dom.find(".dataItem.changedData");
            var $newData = that.dom.find(".dataItem.newData");
            $.each($changedData, function(index, dataItem) {
                dbChangedData.push({
                    id: $(dataItem).attr("data-id"),
                    value: $(dataItem).find(".currentVal").html()
                });
            });
            $.each($newData, function(index, dataItem) {
                newData.push({
                    id: "",
                    value: $(dataItem).find(".currentVal").html()
                });
            });
            if (newData.length > 0) { //添加数据
                //调用封装好的 添加ajax方法
                if (that.options.ajax && that.options.ajax.addUrl) {
                    that.addAjax(newData);
                }
            }
            if (dbChangedData.length > 0) { //更新数据 
                //调用封装好的 修改ajax方法
                if (that.options.ajax && that.options.ajax.editUrl) {
                    that.editAjax(dbChangedData);
                }
            }
        });
    };

    //项目-编辑框 - 点击编辑按钮
    DataList.prototype.edit = function() {
        var that = this;
        that.dom.delegate('.editTextBtn', "click", function() {
            that.initAddOrEditForm(this, that);
        });
    };

    //项目-编辑框，取消按钮
    DataList.prototype.cancelEdit = function() {
        var that = this;
        that.dom.delegate('.cancelTextBtn', "click", function() {
            var $editForm = $(this).closest('.editForm');
            var status = $editForm.attr("data-status"); //标示处于编辑还是前后添加状态
            var newVal = $editForm.find("input[type='text']").val();
            var $dataItem, oldVal;
            switch (status) {
                case "editing":
                    $dataItem = $editForm.next();
                    oldVal = $dataItem.find(".currentVal").html();
                    if (newVal != oldVal) {
                        /*layer.confirm('数据已修改，确认不保存么？', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            $editForm.remove();
                            $dataItem.show();
                            layer.close(index);
                        }, function() {});*/
                        $editForm.remove();
                        $dataItem.show();
                    } else {
                        $editForm.remove();
                        $dataItem.show();
                    }
                    break;
                case "prevAdding":
                case "nextAdding":
                    if (newVal.length > 0) {
                        /*layer.confirm('确认不添加么？', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            $editForm.remove();
                            layer.close(index);
                        }, function() {});*/
                        $editForm.remove();
                    } else {
                        $editForm.remove();
                    }
                    break;
            }
        });
    };
    //项目-编辑框 - 保存按钮
    DataList.prototype.saveEdit = function() {
        var that = this;
        that.dom.delegate('.saveTextBtn', "click", function() {
            var $editForm = $(this).closest('.editForm');
            var newVal = $editForm.find("input[type='text']").val();
            var status = $editForm.attr("data-status"); //标示处于编辑还是前后添加状态
            var $dataItem, $currentVal, oldVal, dbVal;
            switch (status) {
                case "editing":
                    $dataItem = $editForm.next(".dataItem");
                    $currentVal = $dataItem.find(".currentVal");
                    dbVal = $dataItem.attr("data-value"); //数据库中原值

                    if (newVal == "") {
                        layer.confirm('数据不能为空！', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            layer.close(index);
                        }, function() {});
                    } else if (newVal != dbVal) { //如果是与数据库中数据不一致
                        $dataItem.addClass("changedData");
                        $editForm.remove();
                        $currentVal.html(newVal); //重新设置值
                        $dataItem.show();
                    } else {
                        $dataItem.removeClass("changedData");
                        $editForm.remove();
                        $currentVal.html(newVal); //重新设置值
                        $dataItem.show();
                    }
                    break;
                case "prevAdding":
                case "nextAdding":
                    if (newVal.length > 0) {
                        /*layer.confirm('确认添加么？', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            $editForm.replaceWith(that.dataItemHtml("",newVal));
                            layer.close(index);
                        }, function() {});*/
                        $editForm.replaceWith(that.dataItemHtml("", newVal));
                    } else {
                        $editForm.remove();
                    }
                    break;
            }
        });
    };

    //项目-编辑框 - 原值按钮
    DataList.prototype.returnText = function() {
        var that = this;
        that.dom.delegate('.returnTextBtn', "click", function() {
            var $editForm = $(this).closest('.editForm')
            var $dataItem = $editForm.next(".dataItem");
            var dbVal = $dataItem.attr("data-value"); //数据库中原值
            $dataItem.removeClass("changedData");
            $editForm.remove();
            $dataItem.find(".currentVal").html(dbVal); //重新设置值
            $dataItem.show();
        });
    };

    //每一项 html
    DataList.prototype.dataItemHtml = function(id, val) {
        var newData = '';
        if (!id || id == null || id == '') {
            newData = "newData";
        }
        var html = '<div class="dataItem ' + newData + '" data-id="' + id + '" data-value="' + val + '">' +
            '<p class="dataItemOperations">' +
            '<span class="prevAddTextBtn"><img src="assets/img/add.png" ></span>' +
            '<span class="editTextBtn"><img src="assets/img/edit.png" ></span>' +
            '<span class="deleteTextBtn"><img src="assets/img/delete.png"></span>' +
            '<span class="nextAddTextBtn"><img src="assets/img/add.png"></span>' +
            '</p>' +
            '<label>' +
            '<input type="checkbox"><span class="currentVal">' + val + '</span>' +
            '</label>' +
            '</div>';
        return html;
    };

    //添加按钮(+)，
    DataList.prototype.add = function() {
        var that = this;
        that.dom.delegate(".addTextBtn", "click", function() {
            var val = that.$addInput.val();
            if (val.length <= 0) {
                layer.msg('输入数据不能为空！', { icon: 5 });
            } else if (that.checkRepeater(val, that.dom)) {
                layer.msg('有重复数据', { icon: 5 });
            } else {
                $(this).closest(".optionsForm").before(that.dataItemHtml("", val));
                that.$addInput.val("");
            }
        });
    };
    //在数据前面添加一项按钮
    DataList.prototype.prevAdd = function() {
        var that = this;
        that.dom.delegate(".prevAddTextBtn", "click", function() {
            that.initAddOrEditForm(this, that);
        });
    };
    //在数据前面添加一项按钮
    DataList.prototype.nextAdd = function() {
        var that = this;
        that.dom.delegate(".nextAddTextBtn", "click", function() {
            that.initAddOrEditForm(this, that);
        });
    };

    DataList.prototype.initAddOrEditForm = function(thisDom, that) {
        var $dataItem = $(thisDom).closest('.dataItem'),
            $preDataItem;
        var $editForm = that.dom.find(".editForm"),
            status, newVal, oldVal = '';
        if ($editForm.length > 0) {
            status = $editForm.attr("data-status");
            newVal = $editForm.find("input[type='text']").val();
            switch (status) {
                case "editing":
                    $preDataItem = $editForm.next(".dataItem");
                    oldVal = $preDataItem.find(".currentVal").text();
                    if (newVal == oldVal) { //数据没有变化
                        $editForm.remove();
                        $preDataItem.show();
                        that.initForm(that, thisDom, $dataItem);
                    } else {
                        layer.confirm('您还有未保存的数据，确认不保存么？', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            $editForm.remove();
                            $preDataItem.show();
                            that.initForm(that, thisDom, $dataItem);
                            layer.close(index);
                        }, function() {});
                    }
                    break;

                case "prevAdding":
                case "nextAdding":
                    if (newVal.length > 0) {
                        layer.confirm('您还有未保存的数据，确认不保存么？', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            $editForm.remove();
                            that.initForm(that, thisDom, $dataItem);
                            layer.close(index);
                        }, function() {});
                    } else {
                        $editForm.remove();
                        that.initForm(that, thisDom, $dataItem);
                    }
                    break;
            }
        } else {
            that.initForm(that, thisDom, $dataItem);
        }
    };

    //生成编辑框
    DataList.prototype.initForm = function(that, thisDom, $dataItem) {
        var $editForm;
        if ($(thisDom).hasClass("prevAddTextBtn")) {
            $dataItem.before(that.getEditFormHtml(false));
            that.dom.find(".editForm").attr("data-status", "prevAdding");
        } else if ($(thisDom).hasClass("nextAddTextBtn")) {
            $dataItem.after(that.getEditFormHtml(false));
            that.dom.find(".editForm").attr("data-status", "nextAdding");
        } else if ($(thisDom).hasClass("editTextBtn")) {
            $dataItem.before(that.getEditFormHtml(true));
            $dataItem.hide();
            $editForm = that.dom.find(".editForm");
            $editForm.attr("data-status", "editing");
            $editForm.find("input[type='text']").val($dataItem.find(".currentVal").html());
        }
    };

    //检查是否有重复数据
    DataList.prototype.checkRepeater = function(current, dom) {
        var dataItem, val;
        if (typeof current == 'string') {
            val = current;
            dataItem = dom.find(".dataItem");
        } else {
            dataItem = $currenDataItem.siblings(".dataItem");
            val = current.attr("data-value");
        }
        for (var i = 0; i < dataItem.length; i++) {
            var dataitem = dataItem.eq(i);
            if ($(dataitem).attr("data-value") == val) {
                return true;
            }
        }
        return false;
    };

    $.fn.DataList = function(options) {
        //创建DataList的实体
        var dataList = new DataList(this, options);
        //调用(执行)其方法
        return dataList.init();
    }

})(jQuery);
