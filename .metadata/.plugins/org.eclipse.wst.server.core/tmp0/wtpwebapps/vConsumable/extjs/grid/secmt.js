Ext.onReady(function(){
	
	
	Ext.define('security', {
        extend: 'Ext.data.Model',
        fields: [
     //       {name: 'usrid',mapping:'usrid'},
            {name: 'catmt',mapping:'catmt'},
            {name: 'vendmt',mapping:'vendmt'},
            {name: 'deptmt',mapping:'deptmt'},
            {name: 'itemmt',mapping:'itemmt'},
            {name: 'stkmt',mapping:'stkmt'},
            {name: 'details',mapping:'details'},
            {name: 'applyMaterial',mapping:'applyMaterial'},
            {name: 'deptDetails',mapping:'deptDetails'},
            {name: 'deptCostBill',mapping:'deptCostBill'},
            {name: 'printMaterial',mapping:'printMaterial'},
            {name: 'applyStationery',mapping:'applyStationery'},
            {name: 'usrmt',mapping:'usrmt'},
            {name: 'secmt',mapping:'secmt'}
        ]
    });
	
	
	Ext.define('cbModel', {
        extend: 'Ext.data.Model',
        fields: ['val', 'desc']
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
        editable:false,
        
        listeners:{
            change:function(){
                formLoad('Change');
            }
        }
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
                    'details':Ext.getCmp('details').getValue(),
                    'applyMaterial':Ext.getCmp('applyMaterial').getValue(),
                    'deptDetails':Ext.getCmp('deptDetails').getValue(),
                    'deptCostBill':Ext.getCmp('deptCostBill').getValue(),
                    'printMaterial':Ext.getCmp('printMaterial').getValue(),
                    'applyStationery':Ext.getCmp('applyStationery').getValue(),
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
        
        items :[{
        	xtype: 'checkboxgroup',
        	columns : 2,
        	defaultType: 'checkboxfield',
        	items: [
	        	{
		            boxLabel: 'Type maintenance',
		            name: 'catmt',
		            id: 'catmt',
		            inputValue: 'true'
		            
		        },{
		            boxLabel: 'Vendor maintenance',
		            name: 'vendmt',
		            id: 'vendmt'
		        },{
		            boxLabel: 'Department maintenance',
		            name: 'deptmt',
		            id: 'deptmt'
		        },{
		            boxLabel: 'Material maintenance',
		            name: 'itemmt',
		            id: 'itemmt'
		        },{
		            boxLabel: 'Material in/out registration&nbsp;&nbsp;&nbsp;&nbsp;',
		            name: 'stkmt',
		            id: 'stkmt'
		        },{
		            boxLabel: 'Inventory details&nbsp;&nbsp;&nbsp;&nbsp;',
		            name: 'details',
		            id: 'details'
		        },{
		            boxLabel: 'New work clothes Report',
		            name: 'applyMaterial',
		            id: 'applyMaterial'
		        },{
		            boxLabel: 'Department cost details&nbsp;&nbsp;&nbsp;&nbsp;',
		            name: 'deptDetails',
		            id: 'deptDetails'
		        },{
		            boxLabel: 'Department cost Bill',
		            name: 'deptCostBill',
		            id: 'deptCostBill'
		        },{
		            boxLabel: 'Print consumables Report',
		            name: 'printMaterial',
		            id: 'printMaterial'
		        },{
		            boxLabel: 'Stationery Report',
		            name: 'applyStationery',
		            id: 'applyStationery'
		        },{
		            boxLabel: 'User maintenance',
		            name: 'usrmt',
		            id: 'usrmt'
		        },{
		            boxLabel: 'authority maintenance',
		            name: 'secmt',
		            id: 'secmt'
		        }
        	]
        }]
    });
    
    
    function formLoad(action) {
    	    
         var formCmp = Ext.getCmp('secForm');
         var form = formCmp.getForm();
            
         form.load({
              url:'sec',
              params: {
              	  'usrid':Ext.getCmp('usrid').getValue(),
                  sub:action
              }
            /*  ,
              success: function(form, action){
                 console.log(action.result.data);
             }  */
              
         });   
    
    };
    
    
    function setUser() {
    	var params = {
    		'usrid':Ext.getCmp('usrid').getValue(),
    		'sub':'Init'
         };
              
         Ext.Ajax.request({
             params: params,  
             url: 'sec',
             success: function(response){
                 var text = response.responseText;
                 Ext.getCmp('usrid').setValue(text);
             }
        });
    }
    
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
    setUser();
    
})