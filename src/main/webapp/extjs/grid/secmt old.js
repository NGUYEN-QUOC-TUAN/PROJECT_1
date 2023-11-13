Ext.onReady(function(){
	
	
	Ext.define('security', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'usrid',mapping:'usrid'},
            {name: 'catmt',mapping:'catmt'},
            {name: 'vendmt',mapping:'vendmt'},
            {name: 'deptmt',mapping:'deptmt'},
            {name: 'itemmt',mapping:'itemmt'},
            {name: 'stkmt',mapping:'stkmt'},
            {name: 'usrmt',mapping:'usrmt'},
            {name: 'secmt',mapping:'secmt'}
        ]
    });
	
	
	Ext.define('cbModel', {
        extend: 'Ext.data.Model',
        fields: ['val', 'desc']
    });
    
    //定义字段显示值
    var checkStore = Ext.create('Ext.data.Store', {
        fields: ['val', 'name'],
        data : [
            {"val":"true", "name":"available"},
            {"val":"false", "name":"unavailable"}
        ]
    });
    
    
    var catmt = Ext.create('Ext.form.ComboBox', {
   	    fieldLabel: 'Type maintenance',
   	    name: 'catmt',
        id: 'catmt',
        store: checkStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        editable:false
    });
    
    //定义类型下拉列表
    var usrStore = new Ext.data.Store({
        model: 'cbModel',
        proxy: {
            type: 'ajax',
            url : 'sec?sub=bindUser',
            reader: {
                type: 'json',
                root: 'users'
            }
        }
    });
   
    //定义种类组合框 
    var cbusr = Ext.create('Ext.form.ComboBox', {
        name:'usrid',
        id:'usrid',
        fieldLabel: 'account',
        store: usrStore,
        queryMode: 'local',
        displayField: 'desc',
        valueField: 'val',
        editable:false
    });
	
	
	var usrset = Ext.create('Ext.form.FieldSet',{
	   layout: {
            type: 'table'
        },items :[cbusr,
        {
            name: 'save',
            id: 'save',
            xtype: 'button',
            text : 'save',
            handler: function() {
            	var params = {
            		'usrid':Ext.getCmp('usrid').getValue(),
                    'catmt':Ext.getCmp('catmt').getValue(), 
                    'vendmt':Ext.getCmp('vendmt').getValue(),
                    'deptmt':Ext.getCmp('deptmt').getValue(),
                    'itemmt':Ext.getCmp('itemmt').getValue(),
                    'stkmt':Ext.getCmp('stkmt').getValue(),
                    'usrmt':Ext.getCmp('usrmt').getValue(),  
                    'secmt':Ext.getCmp('secmt').getValue(),
                    'sub':'Save'
                };
                    
                    
                Ext.Ajax.request({
                    params: params,  
                    url: 'sec',
                    success: function(response){
                        var text = response.responseText;
                
                        if (text == 'exists') {
                             Ext.MessageBox.alert("exists", "Record already exists, please check."); 
                        } else if (text == 'ok') {
                             Ext.MessageBox.alert("succeeded", "Record writing succeeded");
                        } else if (text == 'failed') {
                             Ext.MessageBox.alert("failed", "Record writing failed");
                        }
                
                    }
                });
            }
        
        }]
	});

	
	//信息编辑显示区域
    var secset = Ext.create('Ext.form.FieldSet', {
        title: 'user authority',
        defaultType: 'combo',
        layout: {
            type: 'table',
            columns: 2
        },
        items :[catmt,
        	{
            fieldLabel: 'Vendor maintenance',
            name: 'vendmt',
            id: 'vendmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        },{
            fieldLabel: 'Department maintenance',
            name: 'deptmt',
            id: 'deptmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        },{
            fieldLabel: 'Material maintenance',
            name: 'itemmt',
            id: 'itemmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        },{
            fieldLabel: 'Material in/out registration&nbsp;&nbsp;&nbsp;&nbsp;',
            name: 'stkmt',
            id: 'stkmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        },{
            fieldLabel: 'User maintenance',
            name: 'usrmt',
            id: 'usrmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        },{
            fieldLabel: 'Authority maintenance',
            name: 'secmt',
            id: 'secmt',
            store: checkStore,
            queryMode: 'local',
            displayField: 'desc',
            valueField: 'val',
            editable:false
        }]
    });
    
    
    function formLoad(action) {
    	    
         var formCmp = Ext.getCmp('secForm');
         var form = formCmp.getForm();
            
         form.load({
              url:'sec',
              params: {
                  sub:action
              }
         });
    
    };
    
     //操作控件定义
    var optPanel = Ext.create('Ext.container.Container', {
        items :[
            usrset,secset
        ]
    });
    
    
    Ext.create('Ext.form.Panel', {
        id:'secForm',
        bodyPadding: '10 10',
        
        frame: true,
        
        width: 400,

        items: [
            optPanel
        ],
       
        reader: new Ext.data.JsonReader({
            type : 'json',
            model: 'security',
            root: 'securitys'
        }),
      
        renderTo: Ext.getBody()
    });
    
    usrStore.load();
    formLoad('Init');
    
 //   optPanel.doLayout(); 
    
})