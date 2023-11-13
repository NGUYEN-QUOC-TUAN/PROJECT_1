// JavaScript Document
Ext.onReady(function(){
	
	var orgid;

    Ext.define('User', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id',mapping:'id'},
            {name: 'name',mapping:'name'},
            {name: 'pwd',mapping:'pwd'},
            {name: 'group',mapping:'group'},
            {name: 'record',mapping:'record'},
            {name: 'total',mapping:'total'}
        ]
    });
    
    
     //定义字段显示值
    var groups = Ext.create('Ext.data.Store', {
        fields: ['val', 'name'],
        data : [
            {"val":"PUR", "name":"PUR"},
            {"val":"ADMIN", "name":"ADMIN"}
        ]
    });
    
   var group = Ext.create('Ext.form.ComboBox', {
   	    fieldLabel: 'group',
   	    id: 'group',
        name: 'group',
        store: groups,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'val',
        readOnly:true,
        editable:false
    });

    //定义字段显示值
    var flds = Ext.create('Ext.data.Store', {
        fields: ['val', 'name'],
        data : [
            {"val":"usr_id", "name":"account"},
            {"val":"usr_name", "name":"name"},
            {"val":"usr_group", "name":"group"}
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
        value:'usr_id'
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
        value:'usr_group'
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
                fld1: 'usr_id',
                cdt1: '=',
                val1: '',
                
                fld2: 'usr_group',
                cdt2: '=',
                val2: ''
            };

            this.up("form").getForm().setValues(cretValues);
            
            formLoad('Reset');
            
        }
    });
    
    
    //查询条件定义
    var findset = Ext.create('Ext.form.FieldSet', {
        title: 'search condition',
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
        
         var formCmp = Ext.getCmp('usrForm');
            var form = formCmp.getForm();
            
            form.load({
                url:'user',
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
    	Ext.getCmp('id').setReadOnly(read);
    	Ext.getCmp('name').setReadOnly(read);
    	Ext.getCmp('pwd').setReadOnly(read);
    	Ext.getCmp('group').setReadOnly(read);
    }
    
    Ext.define('cbModel', {
        extend: 'Ext.data.Model',
        fields: ['val', 'desc']
    });
 
    
    var usrnav =  Ext.create('Ext.container.Container', {
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
            		var usrValues = {
                        id: '',
                        pwd: '',
                        name: '',
                        group: groups.getAt(0).data.val
                       
                    };

                    this.up("form").getForm().setValues(usrValues);
                    orgid = "";
                    Ext.getCmp('record').setValue('-1');
                    setEdit();
            	
            	} else {
            		if (Ext.getCmp('id').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'Please enter the account.');
            		   // Ext.getCmp('nbr').focus(true);
                        return;
            		}
            		
            		if (Ext.getCmp('pwd').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'Please enter a username.');
                        return;
            		}
            		
            		if (Ext.getCmp('pwd').getValue() === '') {
            		    Ext.Msg.alert('Tip', 'Please enter a password.');
                        return;
            		}
            		
            		var params = {
            			'orgid':orgid,
                        'id':Ext.getCmp('id').getValue(), 
                        'name':Ext.getCmp('name').getValue(),
                        'pwd':Ext.getCmp('pwd').getValue(),
                        'group':Ext.getCmp('group').getValue(),  
                        'sub':'Save'
                    };
                    
                    
                    Ext.Ajax.request({
                        params: params,  
                        url: 'user',
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
            		orgid = Ext.getCmp('id').getValue();
            	}
            	setEdit();
               // 
            }
        },{
            name: 'delete',
            id: 'delete',
            text : 'delete',
            handler: function() {
            	
            	Ext.MessageBox.confirm('Delete confirmation', 'Confirm Delete?',function (id) {
                    if (id == 'yes') {
                     	var params = {
                            'id': Ext.getCmp('id').getValue(), 
                            'sub':'Delete'
                        }; 
                        
                        Ext.Ajax.request({
                            params: params,  
                            url: 'user',
            
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
        }]
    });
    
    //信息编辑显示区域
    var optset = Ext.create('Ext.form.FieldSet', {
        title: 'user information',
        defaultType: 'textfield',
        layout: {
            type: 'table',
            columns: 2
        },
        items :[{
            fieldLabel: 'account',
            name: 'id',
            id: 'id',
            maxLength:8,
            readOnly:true
        },{
            fieldLabel: 'username',
            name: 'name',
            id: 'name',
            maxLength:8,
            readOnly:true
        }, {
            fieldLabel: 'password',
            name: 'pwd',
            id: 'pwd',
            maxLength:8,
            readOnly:true
        },group,usrnav]
    });
    
    
    //操作控件定义
    var optPanel = Ext.create('Ext.container.Container', {
        items :[
            optset,findset
        ]
    });

    Ext.create('Ext.form.Panel', {
        id:'usrForm',
        bodyPadding: '10 10',
        
        layout: {
            type: 'table',
            columns: 2
        },
        
        frame: true,
        
        width: 560,

        items: [
            optPanel
        ],
       
        reader: new Ext.data.JsonReader({
            type : 'json',
            model: 'User',
            root: 'users'
        }),
      
        renderTo: Ext.getBody()
    });
    
   formLoad('Init');
   
    
   
});