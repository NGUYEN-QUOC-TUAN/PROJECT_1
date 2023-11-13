Ext.onReady(function() {
	
	Ext.define('materialModel', {
	     extend: 'Ext.data.Model',
	     fields: [
	         {name: 'tx_item', type: 'string'},
	         {name: 'sum', type: 'int'},
	         {name: 'item_desc', type: 'string'},
	         {name: 'item_std', type: 'string'},
	         {name: 'item_cost', type: 'string'},
	         {name: 'item_um', type: 'string'},
	         {name: 'dept_desc', type: 'string'}
	     ],
	     proxy : getProxy(ctx+'/details!getList')
	});
	
	var optSet = Ext.create('Ext.form.FieldSet', {
        layout: {
            type: 'table',
            columns: 4
        },
        defaults: {
            xtype: 'button'
        },
        items: [{xtype:'datefield',
        	fieldLabel: 'Date',
        	labelWidth : 40,
        	name: 'reportDate',
        	id :'reportDate',
            format: "Y-m-d",
            maxValue: new Date()
        },{xtype:'numberfield',
        	fieldLabel: 'Starting Part NO.',
        	name: 'materiel',
        	id : 'materiel' ,
            minValue: 0
        },{text:"Confirm",
        	handler: function(){
        		if(isValid()){
        			loadGrid();
        		}
        	}
        },{text:"Export",
        	handler: function(){
        		if(isValid()){
        			window.open(ctx+'/details!output?reportDate=' + Ext.Date.format(Ext.getCmp('reportDate').value, 'Y-m-d') + '&materiel=' + Ext.getCmp('materiel').getValue());
        		}
        	}
        }
        ]
    });
    
    function isValid(){
    	if(Ext.getCmp('reportDate').value == null){
    		alert("please enter the time.");
    		return false;
    	}
    	if(Ext.getCmp('materiel').value == null){
    		alert("please enter the starting Part NO.");
    		return false;
    	}
    	return true;
    }
    
    var tlb = Ext.create('Ext.toolbar.Toolbar', {
        width   : 700,
        items: [optSet]
    });
	
	var detailsGridStore = Ext.create('Ext.data.Store', {
		model: 'materialModel',
		pageSize:50
	});
	
	Ext.define('materialList_grid',{
	 	extend:'Ext.grid.Panel',
	 	tbar:tlb,
		id:'materialList_grid',
	    store: detailsGridStore,
	    columns: [
	        { header: 'Item No', dataIndex: 'tx_item'},
	        { header: 'Description', dataIndex: 'item_desc'},
	        { header: 'Standard', dataIndex: 'item_std' },
	        { header: 'Unit cost', dataIndex: 'item_cost'},
	        { header: 'um', dataIndex: 'item_um' },
	        { header: 'Bal Qty', dataIndex: 'sum' }
	    ]
	});
	function loadGrid(){
		detailsGridStore.getProxy().extraParams = {reportDate :Ext.getCmp('reportDate').getValue() , materiel :Ext.getCmp('materiel').getValue()};
		detailsGridStore.loadPage(1);
	}
	
	var materialList=new materialList_grid({renderTo: Ext.getBody(),height: Ext.getBody().getHeight() - 10});
	
});