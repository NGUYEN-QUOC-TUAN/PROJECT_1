// JavaScript Document
Ext.onReady(function(){
	
	var orgnbr;

    Ext.define('Item', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'nbr',mapping:'nbr'},
            {name: 'desc',mapping:'desc'},
            {name: 'std',mapping:'std'},
            {name: 'cat',mapping:'cat'},
            {name: 'cost',mapping:'cost'},
            {name: 'um',mapping:'um'},
            {name: 'vend',mapping:'vend'},
            {name: 'lvl',mapping:'lvl'},
            {name: 'record',mapping:'record'},
            {name: 'total',mapping:'total'},
            {name: 'curr',mapping:'curr'},
            {name: 'moq',mapping:'moq'}
        ]
    });

    //定义字段显示值
    var flds = Ext.create('Ext.data.Store', {
        fields: ['val', 'name'],
        data : [
            {"val":"item_num", "name":"Item No"},
            {"val":"item_desc", "name":"Name"},
            {"val":"item_std", "name":"Standard"},
            {"val":"item_cat", "name":"Type"},
            {"val":"item_um", "name":"Um"},
            {"val":"item_vendor", "name":"Vendor"},
            {"val":"item_curr", "name":"Current"},
            {"val":"item_moq", "name":"Moq"}
        ]
    });

    //定义查询字段控件 
    var cbfld1 = Ext.create('Ext.form.ComboBox', {
        name:'fld1',
        fieldLabel: '',
        store: flds,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        editable:false,
        value:'item_num'
    });
    
    //定义查询字段控件 
    var cbfld2 = Ext.create('Ext.form.ComboBox', {
        name:'fld2',
        fieldLabel: '',
        store: flds,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        editable:false,
        value:'item_num'
    });
    
    //定义条件显示值
    var cdts = Ext.create('Ext.data.Store', {
        fields: ['val', 'name'],
        data : [
            {"val":"=", "name":"="},
            {"val":">", "name":">"},
            {"val":"<", "name":"<"},
            {"val":">=", "name":">="},
            {"val":"<=", "name":"<="},
            {"val":"like", "name":"Like"}
        ]
    });
    
    var cbcdt1 = Ext.create('Ext.form.ComboBox', {
        name:'cdt1',
        fieldLabel: '',
        store: cdts,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        editable:false,
        value:'='
    });
    
    var cbcdt2 = Ext.create('Ext.form.ComboBox', {
        name:'cdt2',
        fieldLabel: '',
        store: cdts,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        editable:false,
        value:'='
    });
    
    
    var txtfld1 = Ext.create('Ext.form.field.Text',{
        name:'val1',
        emptyText: 'query value'
    });
    
     var txtfld2 = Ext.create('Ext.form.field.Text',{
        name:'val2',
        emptyText: 'query value'
    });
    
    var btnFind = Ext.create('Ext.Button', {
        text: 'search',       
        handler: function() {
            formLoad('Find');
        }
    });
    
    var btnReset = Ext.create('Ext.Button', {
        text: 'reset',       
        handler: function() {

            var cretValues = {
                fld1: 'item_num',
                cdt1: '=',
                val1: '',
                
                fld2: 'item_num',
                cdt2: '=',
                val2: ''
            };

            this.up("form").getForm().setValues(cretValues);
            
            formLoad('Reset');
            
        }
    });
    
    
    //查询条件定义
    var findset = Ext.create('Ext.form.FieldSet', {
        title: 'query conditions',
        layout: {
            type: 'table',
            columns: 4
        },
        items :[
            cbfld1,cbcdt1,txtfld1,btnFind,
            cbfld2,cbcdt2,txtfld2,btnReset
        ]
    });
    
    
    function formLoad(action) {
    
         if ((txtfld1.getValue() === '') 
         && (txtfld2.getValue() === '')
         && (action === 'Find')) {
                Ext.Msg.alert('Tip', 'Please enter the query value');
                return;
         }
        
         var formCmp = Ext.getCmp('itemForm');
            var form = formCmp.getForm();
            
            form.load({
                url:'item',
               params: {
                    sub:action,
                    fld1:cbfld1.getValue(),
                    fld2:cbfld2.getValue(),
                    cdt1:cbcdt1.getValue(),
                    cdt2:cbcdt2.getValue(),
                    val1:txtfld1.getValue(),
                    val2:txtfld2.getValue(),
                    record:Ext.getCmp('record').getValue(),
                    total:Ext.getCmp('total').getValue()
                }
            });
    
    }
    
    function setEdit() {
    	if (Ext.getCmp('new').getText() === 'add') {
            Ext.getCmp('new').setText('save');
            Ext.getCmp('modify').setText('cancel');
            Ext.getCmp('first').setDisabled(true);
            Ext.getCmp('prev').setDisabled(true);
            Ext.getCmp('next').setDisabled(true);
            Ext.getCmp('last').setDisabled(true);
            Ext.getCmp('delete').setDisabled(true);
            btnFind.setDisabled(true);
            btnReset.setDisabled(true);
            setReadOnly(false);
        } else {
            Ext.getCmp('new').setText('add');
            Ext.getCmp('modify').setText('modify');
            Ext.getCmp('first').setDisabled(false);
            Ext.getCmp('prev').setDisabled(false);
            Ext.getCmp('next').setDisabled(false);
            Ext.getCmp('last').setDisabled(false);
            Ext.getCmp('delete').setDisabled(false);
            btnFind.setDisabled(false);
            btnReset.setDisabled(false);
            setReadOnly(true);
        }
    
    }
    
    function setReadOnly(read) {
    	Ext.getCmp('nbr').setReadOnly(read);
    	Ext.getCmp('desc').setReadOnly(read);
    	Ext.getCmp('std').setReadOnly(read);
 	  	Ext.getCmp('cat').setReadOnly(read);
    	Ext.getCmp('cost').setReadOnly(read);
    	Ext.getCmp('curr').setReadOnly(read);
    	Ext.getCmp('um').setReadOnly(read);
    	Ext.getCmp('vend').setReadOnly(read);
    	Ext.getCmp('lvl').setReadOnly(read);
    	Ext.getCmp('moq').setReadOnly(read);
    }
    
    Ext.define('cbModel', {
        extend: 'Ext.data.Model',
        fields: ['val', 'desc']
    });
    
    //定义类型下拉列表
    var catStore = new Ext.data.Store({
        model: 'cbModel',
        proxy: {
            type: 'ajax',
            url : 'item?sub=bindCat',
            reader: {
                type: 'json',
                root: 'categorys'
            }
        }
    });
   
    //定义种类组合框 
    var cbcat = Ext.create('Ext.form.ComboBox', {
        name:'cat',
        id:'cat',
        fieldLabel: 'type',
        store: catStore,
        queryMode: 'local',
        displayField: 'desc',
        valueField: 'val',
        editable:false,
        readOnly:true
    });
    
    var vendStore = new Ext.data.Store({
        model: 'cbModel',
        proxy: {
            type: 'ajax',
            url : 'item?sub=bindVend',
            reader: {
                type: 'json',
                root: 'vendors'
            }
        }
    });
    var currStore = new Ext.data.Store({
        model: 'cbModel',
        proxy: {
            type: 'ajax',
            url : 'item?sub=bindCurr',
            reader: {
                type: 'json',
                root: 'currs'
            }
        }
    });
    
    var cbcurr = Ext.create('Ext.form.ComboBox', {
        name:'curr',
        id:'curr',
        fieldLabel: 'currency',
        store: currStore,
        queryMode: 'local',
        displayField: 'val',
        valueField: 'val',
        editable:false,
        readOnly:true
    });
    
    //定义供应商组合框 
    var cbvend = Ext.create('Ext.form.ComboBox', {
        name:'vend',
        id:'vend',
        fieldLabel: 'vendor',
        store: vendStore,
        queryMode: 'local',
        displayField: 'desc',
        valueField: 'val',
        editable:false,
        readOnly:true
    });
    
    function bindGroup() {
    	Ext.Ajax.request({
            url: 'item?sub=bindGroup',
            success: function(response){
            	Ext.getCmp('group').setValue(response.responseText);
            }
        });
    }
    
    var itemnav =  Ext.create('Ext.container.Container', {
        colspan: 2,
        layout: {
            type: 'hbox'
        },
      
        defaults: {
            xtype: 'button'
        },
        items: [{
            name: 'first',
            id: 'first',
            text : 'first',
            handler: function() {
                formLoad('First');
            }
        },{
            name: 'prev',
            id: 'prev',
            text : 'previous',
            handler: function() {
                formLoad('Prev');
            }
        },{
            name: 'next',
            id: 'next',
            text : 'next',
            handler: function() {
                formLoad('Next');
            }
        },{
            name: 'last',
            id: 'last',
            text : 'last',
            handler: function() {
                formLoad('Last');
            }
        },{
            name: 'new',
            id: 'new',
            text : 'add',
            handler: function() {
            	if (Ext.getCmp('new').getText() === 'add') {
            		var itemValues = {
                        nbr: '',
                        desc: '',
                        std: '',
                        cat: '',
                        curr: '',
                        cost: '',
                        um: '',
                        vend: '',
                        lvl: '',
                        moq: ''
                    };

                    this.up("form").getForm().setValues(itemValues);
                    orgnbr = "";
                    Ext.getCmp('record').setValue('-1');
                    setEdit();
            	
            	} else {
            		if (Ext.getCmp('nbr').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please enter the No.');
            		   // Ext.getCmp('nbr').focus(true);
                        return;
            		}
            		
            		if (Ext.getCmp('desc').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please enter a description.');
                        return;
            		}
            		
            		if (Ext.getCmp('std').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please enter the standard.');
                        return;
            		}
            		
            		if (Ext.getCmp('cat').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please select type.');
                        return;
            		}
            		if (Ext.getCmp('curr').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please enter the currency.');
                        return;
            		}
            		if (Ext.getCmp('cost').getValue() === '' || Ext.getCmp('cost').getValue() === null)  {
            		    Ext.Msg.alert('Tip', 'please enter the price.');
                        return;
            		}
            		if (Ext.getCmp('um').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please enter the unit.');
                        return;
            		}
            		if (Ext.getCmp('vend').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'please select vendor.');
                        return;
            		}
            		if (Ext.getCmp('moq').getValue() === '' || Ext.getCmp('moq').getValue() === null) {
            		    Ext.Msg.alert('Tip', 'please enter the MOQ.');
                        return;
            		}
            		if (Ext.getCmp('lvl').getValue() === '' || Ext.getCmp('lvl').getValue() === null) {
            		    Ext.Msg.alert('Tip', 'please enter the safety stock.');
                        return;
            		}
            		var params = {
            			'orgnbr':orgnbr,
                        'nbr':Ext.getCmp('nbr').getValue(),  
                        'desc':Ext.getCmp('desc').getValue(),
                        'std':Ext.getCmp('std').getValue(),  
                        'cat':Ext.getCmp('cat').getValue(),
                        'cost':Ext.getCmp('cost').getValue(),  
                        'um':Ext.getCmp('um').getValue(),
                        'curr':Ext.getCmp('curr').getValue(),
                        'vend':Ext.getCmp('vend').getValue(),  
                        'lvl':Ext.getCmp('lvl').getValue(),
                        'moq':Ext.getCmp('moq').getValue(),
                        'sub':'Save'
                    };
                    
                    
                    Ext.Ajax.request({
                        params: params,  
                        url: 'item',
                        success: function(response){
                            var text = response.responseText;
                
                            if (text == 'exists') {
                                Ext.MessageBox.alert("exists", "Record already exists, please check."); 
                            } else if (text == 'ok') {
                                Ext.MessageBox.alert("succeeded", "Record writing succeeded");
                                setEdit();
                            } else if (text == 'failed') {
                                Ext.MessageBox.alert("failed", "Record writing failed");
                            }
                
                       }
                   });
            		
            	}
            }
        },{
            name: 'modify',
            id: 'modify',
            text : 'modify',
            handler: function() {
            	if (Ext.getCmp('modify').getText() === 'cancel') {
            		formLoad('Cancel');
            	} else {
            		orgnbr = Ext.getCmp('nbr').getValue();
            	}
            	setEdit();
               // 
            }
        },{
            name: 'delete',
            id: 'delete',
            text : 'delete',
            handler: function() {
            	
            	Ext.MessageBox.confirm('Delete confirmation', 'Confirm delete?',function (id) {
                    if (id == 'yes') {
                     	var params = {
                            'nbr': Ext.getCmp('nbr').getValue(), 
                            'sub':'Delete'
                        }; 
                        
                        Ext.Ajax.request({
                            params: params,  
                            url: 'item',
            
                            success: function(response){
                                var text = response.responseText;
                
                                if (text == 'ok') {
                                    Ext.MessageBox.alert("Delete succeeded", "Record deleted successfully");
                                    formLoad('Init'); 
                                } 
                
                            }
        
                         });
                    }
                    
                });
            	
            }
        },{
            xtype: 'textfield',
            width: 50,
            name: 'record',
            id: 'record',
            readOnly:true,
            value : ''
        },{
            xtype: 'label',
            text: '/'
        },{
            xtype: 'textfield',
            width: 50,
            name: 'total',
            id: 'total',
            readOnly:true,
            value : ''
        },{
        	xtype: 'hiddenfield',
        	name: 'group',
        	id: 'group',
        	value: '' 
        }]
    });
    
    //信息编辑显示区域
    var optset = Ext.create('Ext.form.FieldSet', {
        title: 'Material information',
        defaultType: 'textfield',
        layout: {
            type: 'table',
            columns: 3
        },
        items :[{
            fieldLabel: 'Item No',
            name: 'nbr',
            id: 'nbr',
            maxLength:18,
            readOnly:true,
            listeners:{
            	blur:function(){
            		//要求有点.... 没有办法只能HardCode
            		if (Ext.getCmp('group').getValue() == 'PUR') {
            			if (Ext.getCmp('nbr').getValue().length >= 7 && Ext.getCmp('nbr').readOnly == false) {
            		        Ext.getCmp('cat').setValue(Ext.getCmp('nbr').getValue().substring(4,7));
            		    }  
            		} else if (Ext.getCmp('group').getValue() == 'ADMIN') {
            			if (Ext.getCmp('nbr').getValue().length >= 2 && Ext.getCmp('nbr').readOnly == false) {
            		        Ext.getCmp('cat').setValue(Ext.getCmp('nbr').getValue().substring(0,2));
            		    }
            		}	
            		         	
            	}
            }
        }, {
            fieldLabel: 'name',
            name: 'desc',
            id: 'desc',
            maxLength:40,
            readOnly:true
        },{
            fieldLabel: 'standard',
            name: 'std',
            id: 'std',
            maxLength:40,
            readOnly:true
        },cbcat,{
            fieldLabel: 'unit-price',
            name: 'cost',
            id: 'cost',
            xtype: 'numberfield',
            readOnly:true
        },{
            fieldLabel: 'um',
            name: 'um',
            id: 'um',
            maxLength:6,
            readOnly:true
        },cbcurr,cbvend,{
            fieldLabel: 'safety stock',
            name: 'lvl',
            id: 'lvl',
            xtype: 'numberfield',
            readOnly:true
        },itemnav, {
	        fieldLabel: 'MOQ',
	        name: 'moq',
	        id: 'moq',
	        readOnly: true,
	        xtype: 'numberfield',
    }],
    });
    
    
    //操作控件定义
    var optPanel = Ext.create('Ext.container.Container', {
        items :[
            optset,findset
        ]
    });

    Ext.create('Ext.form.Panel', {
    //    title: '物料维护',
        id:'itemForm',
        bodyPadding: '10 10',
        
        layout: {
            type: 'table',
            columns: 3
        },
        
        frame: true,
        
        width: 820,

        items: [
            optPanel
        ],
       
        reader: new Ext.data.JsonReader({
            type : 'json',
            model: 'Item',
            root: 'items'
        }),
      
        renderTo: Ext.getBody()
    });
    
   bindGroup(); 
   catStore.load();
   vendStore.load(); 
   currStore.load();
   formLoad('Init');

      
    //导航键盘
  //  var el = optset;  
  /*  var nav = new Ext.util.KeyNav(Ext.getDoc(),{
    	left : function(e) {
    		alert('向左的按键被按下了');    	
    	},
    	right : function(e) {
    		alert('向右的按键被按下了');    	
    	},
    	enter : function(e) {
    	    e.stopEvent();
    		e.keyCode=9;
    		alert(e.toString());    	
    	},
    	scope: optset
    });   */
    
   /* itemnav.enable();   */
    
   
});