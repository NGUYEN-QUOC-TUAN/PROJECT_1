Ext.onReady(function(){
	
	
	var login = new Ext.FormPanel({
	    labelWidth : 75,
	    monitorValid : true,// 把有formBind:true的按钮和验证绑定
	    baseCls : 'x-plain',
        
        defaultType : 'textfield',// 默认字段类型
        
        items: [{
            fieldLabel : 'account',
            name : 'name',// 元素名称
            // anchor:'95%',//也可用此定义自适应宽度
            allowBlank : false,// 不允许为空
            blankText : 'Account cannot be empty!'// 错误提示内容
        }, {
            inputType : 'password',
            fieldLabel : 'password',
            // anchor:'95%',
            name : 'pwd',
            allowBlank : false,
            blankText : 'Password cannot be empty!',
            listeners : {
            	specialkey : function(field, e) {  
                    if (e.getKey() == Ext.EventObject.ENTER) {
                        loginSystem();
                    }
            	}
            }
        }],
        
        buttons : [{
            text : 'login',
            formBind : true,
            type : 'submit',
            handler : function() {
                loginSystem();
            }
       },{
            text : 'cancel',
            handler : function() {
              login.form.reset();
            }
        }]
    });
    
    //登入系统
    function loginSystem() {
    	
    	login.form.doAction('submit', {
            url : 'login',
            method : 'post',
            params : '',
            success : function(form, action) {
                if (action.result.msg == 'ok') {
                    window.location = 'index.html';
                } else {
                    Ext.Msg.alert('Login failed',action.result.msg);
                }
            },
            // 提交失败的回调函数
            failure : function() {
                Ext.Msg.alert('Error','An error occurred on the server. Please try again later！');
            }
        });
    	
    }
    
    
    // 定义窗体
    var win = new Ext.Window({
        id : 'loginWin',
        title : 'User Login',
        layout : 'fit', // 布局方式fit，自适应布局
        width : 300,
        height : 150,
        modal : true,
        plain : true,
        bodyStyle : 'padding:5px;',
        maximizable : false,// 禁止最大化
        closeAction : 'close',
        closable : false,// 禁止关闭
        collapsible : true,// 可折叠
        plain : true,
        buttonAlign : 'center',
        items : login
        // 将表单作为窗体元素嵌套布局\
    });
    
    win.show();// 显示窗体
    

})