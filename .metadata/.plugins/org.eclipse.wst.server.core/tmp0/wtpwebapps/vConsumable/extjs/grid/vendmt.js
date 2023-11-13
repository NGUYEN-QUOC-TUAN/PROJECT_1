// JavaScript Document
Ext.onReady(function(){ 
    
	var orgcode;
	var row,newrecord;
    // Set up a model to use in our Store
    Ext.define('Vend', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'code', type: 'string'},
            {name: 'desc',  type: 'string'}
        ]
    });
    
    var store = new Ext.data.Store({
        storeId:'simpsonsStore',
        model: 'Vend',
        proxy: {
            type: 'ajax',
            url : 'vend?option=load',
            method:'POST',
            reader: {
                type: 'json',
                root: 'roots'
            }
        }
    });
    
    store.load();
    
    var tlb = Ext.create('Ext.toolbar.Toolbar', {
        width   : 700,
        items: [{
            iconCls: 'icon-add',
            text: 'Add',
            scope: this,
            handler: function(){
                var rec = new Vend({
                    code: '',
                    desc: ''
                }), edit = editing;
                
                edit.cancelEdit();
                
                if (newrecord  > 0) {} 
                else {
                	store.insert(0, rec);
                	newrecord = 1;
                }
                
                edit.startEdit(0, 0);
                orgcode = "";
                row = 0;
            }
        },{
            iconCls: 'icon-delete',
            text: 'Delete',
            disabled: true,
            itemId: 'delete',
            scope: this,
            handler: function(){
              //  alert("删除按钮");
                Ext.MessageBox.confirm('Delete confirmation', 'Confirm Delete?',function (id) {
                    if (id == 'yes') {
                     	var selection = grid.getSelectionModel().getSelection()[0];
                        if (selection) {
                        	
                            var params = {
                            	'code': selection.data.code,  
                            	'option':'delete'
                            };
        
                            Ext.Ajax.request({
                                params: params,  
                                url: 'vend',
            
                                success: function(response){
                                    var text = response.responseText;
                
                                    if (text == 'ok') {
                                        Ext.MessageBox.alert("Delete succeeded", "Record deleted successfully");
                                        if (row == 0 && newrecord > 0)
                    	                    newrecord = 0;
                                        store.remove(selection);
                                    } 
                
                                }
        
                            });
            
                        }
                    }
                });
            }
        }]
        
    });

    var editing = Ext.create('Ext.grid.plugin.RowEditing', {
    	autoCancel:false,
        clicksToEdit: 2
    });
    
    //创建Grid
    var grid = Ext.create('Ext.grid.Panel', {
        title: 'Vendor Information',
        store: Ext.data.StoreManager.lookup('simpsonsStore'),
        columns: [
            {header: 'Vendor No',  dataIndex: 'code', 
                field:{
                    xtype:'textfield',
                    maxLength: 5,
                    allowBlank:false
                }
            },
            {header: 'Vendor Name', dataIndex: 'desc', flex:1, 
                field:{
                    xtype:'textfield',
                    maxLength: 40
                }
            }
        ],
        selType: 'rowmodel',
        plugins: [editing],
        height: Ext.getBody().getHeight(),
    //    width: 100,
        tbar:tlb,
        listeners: {
            selectionchange: function(model, records) {
                if (records[0]) {
                	orgcode = records[0].data.code;
                
                    //获取选中的行号
                    row = grid.getStore().indexOf(records[0]);
                
                    this.down('#delete').setDisabled(false);
                } else {
                	this.down('#delete').setDisabled(true);
                }
            }
        },
        renderTo: Ext.getBody()
    });
    
    function onEdit(e) {
    	//检查记录是否存在,不存在就提交    	
        var params = {
        	'orgcode':orgcode,
            'code': e.record.data.code,  
            'desc': e.record.data.desc,
            'option':'update'  
        };
        
        
        Ext.Ajax.request({
            params: params,  
            url: 'vend',
            success: function(response){
                var text = response.responseText;
                
                if (text == 'exists') {
                    Ext.MessageBox.alert("exists", "Record already exists, please check."); 
                    e.record.reject();
                    editing.startEdit(row,0);
                } else if (text == 'ok') {
                    Ext.MessageBox.alert("succeeded", "Record writing succeeded");
                    e.record.commit();
                    if (row == 0 && newrecord > 0)
                    	newrecord = 0;
                } else if (text == 'failed') {
                    Ext.MessageBox.alert("failed", "Record writing failed");
                    e.record.reject();
                }
                
            }
        });
        
    }
    
    grid.on('edit', onEdit, this);
     
});