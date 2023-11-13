var home = Ext.create('Ext.panel.Panel', {

    items: [{
	        xtype: 'label', 
	        forId: 'welcome', 
	        html: '<div class="headpage">Welcome to the auxiliary material management system<div/>'
	    },{xtype: 'button', 
		    text: 'Exit',
		    renderTo: Ext.getBody(),
		    handler: function() {
		        window.location.href='logout';
		    }
	    }]
});

var tab = Ext.create('Ext.tab.Panel', {
       
     activeTab: 0,
     items: [{
         title: 'Home',
         bodyPadding: 10,
         closable: true,
         items: [home]
     }]
});

var treeStore = Ext.create('Ext.data.TreeStore',{
	proxy: {
		type: 'ajax',
		url: 'tree'
	}
});
	
var tree = Ext.create('Ext.tree.Panel',{
	store: treeStore,
	rootVisible: false,
	width:230,
	listeners: {
	    itemclick: function (view, record, item, index, e, eOpts) {
	    	if (record.get('leaf')) { //叶子节点
                var id = record.get('id');
                if (Ext.getCmp(id))
                    tab.setActiveTab(id);
                else { 
                     tab.add({
                        id: id,
                     	title: record.get('text'),
                     	html:  "<iframe width='100%' height='100%' frameborder='0' src='"+ id +"'></iframe>",
                     	closable: true
                     });
                     tab.setActiveTab(id);
                }
            }
	    }
	}

});


Ext.onReady(function(){

    // setup the state provider, all state information will be saved to a cookie
    Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

    Ext.create('Ext.container.Viewport', {
    	layout: 'border',
        padding: '5',
    	
        items: [{
            region: 'west',
            title: 'System Function',
            width: 230,
            stateId: 'treePanel',
            split: true,
            collapsible: true,
            items: [tree]
        }, {
            region: 'center',
            stateId: 'tabPanel',
            items: [tab]
        }]
    });
    
    tree.setHeight(Ext.getBody().getHeight());
    tab.setHeight(Ext.getBody().getHeight());
    home.setHeight(tab.getHeight() - 10);
    
 //   window.location = 'login.html';
});
