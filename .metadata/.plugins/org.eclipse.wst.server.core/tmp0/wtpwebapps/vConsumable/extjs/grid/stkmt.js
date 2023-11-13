// JavaScript Document
Ext.onReady(function() {

	var orgdept, orgdate, orgnbr;
	var row, newrecord;
	var params;

	var itemsPerPage = 30;

	// Set up a model to use in our Store
	Ext.define('Stock', {
		extend: 'Ext.data.Model',
		fields: [
			{ name: 'lot', type: 'string' },
			{ name: 'dept', type: 'string' },
			{ name: 'date', type: 'date', dateFormat: 'Y-m-d' },
			{ name: 'nbr', type: 'string' },
			{ name: 'desc', type: 'string' },
			{ name: 'qty', type: 'number' },
			{ name: 'current', type: 'string' },
			{ name: 'cost', type: 'number' },
			{ name: 'rmk', type: 'string' },
			{ name: 'um', type: 'string' },
			{ name: 'expiry_date', type: 'date', dateFormat: 'Y-m-d' },
			{ name: 'po', type: 'string' },
			{ name: 'vendor', type: 'string' },
			{ name: 'vendor_lot', type: 'string' },
			{ name: 'label_printed', type: 'string' }
		]
	});

	var store = new Ext.data.Store({
		storeId: 'stockStore',
		model: 'Stock',
		autoLoad: false,
		pageSize: itemsPerPage,
		proxy: {
			type: 'ajax',
			url: 'stock',
			method: 'POST',
			reader: {
				type: 'json',
				root: 'stocks',
				totalProperty: 'total'
			}
		}
	});
	//定义字段显示值
	var flds = Ext.create('Ext.data.Store', {
		fields: ['val', 'name'],
		data: [
			{ "val": "tx_dept", "name": "Department No." },
			{ "val": "tx_date", "name": "Date" },
			{ "val": "tx_item", "name": "Item No." }
		]
	});

	//定义查询字段控件 
	var cbfld1 = Ext.create('Ext.form.ComboBox', {
		name: 'fld1',
		fieldLabel: '',
		store: flds,
		queryMode: 'local',
		displayField: 'name',
		valueField: 'val',
		editable: false,
		value: 'tx_dept'
	});
	var cbfld4 = Ext.create('Ext.data.Store', {
		fields: ['desc', 'val'],
		data: [
			{ desc: 'Y', val: 'Y' },
			{ desc: 'N', val: 'N' }
		]
	});
	//定义查询字段控件 
	var cbfld2 = Ext.create('Ext.form.ComboBox', {
		name: 'fld2',
		fieldLabel: '',
		store: flds,
		queryMode: 'local',
		displayField: 'name',
		valueField: 'val',
		editable: false,
		value: 'tx_dept'
	});

	//定义条件显示值
	var cdts = Ext.create('Ext.data.Store', {
		fields: ['val', 'name'],
		data: [
			{ "val": "=", "name": "=" },
			{ "val": ">", "name": ">" },
			{ "val": "<", "name": "<" },
			{ "val": ">=", "name": ">=" },
			{ "val": "<=", "name": "<=" },
			{ "val": "like", "name": "Like" }
		]
	});
	var cbcdt1 = Ext.create('Ext.form.ComboBox', {
		name: 'cdt1',
		fieldLabel: '',
		store: cdts,
		queryMode: 'local',
		displayField: 'name',
		valueField: 'val',
		editable: false,
		value: '='
	});

	var cbcdt2 = Ext.create('Ext.form.ComboBox', {
		name: 'cdt2',
		fieldLabel: '',
		store: cdts,
		queryMode: 'local',
		displayField: 'name',
		valueField: 'val',
		editable: false,
		value: '='
	});


	var txtfld1 = Ext.create('Ext.form.field.Text', {
		name: 'val1',
		emptyText: 'query value'
	});

	var txtfld2 = Ext.create('Ext.form.field.Text', {
		name: 'val2',
		emptyText: 'query value'
	});

	var btnFind = Ext.create('Ext.Button', {
		text: 'search',
		handler: function() {

			if ((txtfld1.getValue() === '')
				&& (txtfld2.getValue() === '')) {
				Ext.Msg.alert('Tip', 'Please enter the query value');
				return;
			}

			Ext.getCmp('sum').setValue('');

			store.loadPage(1);

			sumTotal();

		}
	});

	var btnReset = Ext.create('Ext.Button', {
		text: 'reset',
		handler: function() {
			cbfld1.setValue('tx_dept');
			cbfld2.setValue('tx_dept');
			cbcdt1.setValue('=');
			cbcdt2.setValue('=');
			txtfld1.setValue('');
			txtfld2.setValue('');
			Ext.getCmp('sum').setValue('');
			store.loadPage(1);

		}
	});


	//查询条件定义
	var findset = Ext.create('Ext.form.FieldSet', {
		title: 'search condition',
		layout: {
			type: 'table',
			columns: 4
		},
		items: [
			cbfld1, cbcdt1, txtfld1, btnFind,
			cbfld2, cbcdt2, txtfld2, btnReset
		]
	});

	var optset = Ext.create('Ext.form.FieldSet', {
		title: '',
		layout: {
			type: 'table',
			columns: 4
		},
		defaults: {
			xtype: 'button'
		},
		items: [{
			iconCls: 'icon-add',
			text: 'Add',
			// scope: this,
			handler: function() {
				var dept = null;
				if (store.getAt(0) == undefined || store.getAt(0) == null) {
					dept = '';
				} else {
					dept = store.getAt(0).data.dept;
				}
				var rec = new Stock({
					dept: deptStore.getAt(0).data.val,
					date: new Date(),
					nbr: '',
					desc: '',
					qty: '',
					current: currStore.getAt(0).data.val,
					cost: '',
					rmk: '',
					um: '',
					po: '',
					expiry_date: new Date(),
					label_printed: cbfld4.getAt(0).data.val,

				}), edit = editing;

				edit.cancelEdit();

				if (newrecord > 0) { }
				else {
					store.insert(0, rec);
					newrecord = 1;
				};

				edit.startEdit(0, 0);
				orgdept = "";
				orgdate = "";
				orgnbr = "";
				row = 0;
			}
		}, {
			iconCls: 'icon-delete',
			text: 'Delete',
			disabled: true,
			itemId: 'delete',
			//    scope: this,
			handler: function() {

				Ext.MessageBox.confirm('Delete confirmation', 'Confirm Delete?', function(id) {
					if (id == 'yes') {

						var selection = grid.getSelectionModel().getSelection()[0];
						if (selection) {

							var params = {
								'dept': selection.data.dept,
								'date': selection.data.date,
								'nbr': selection.data.nbr,
								'sub': 'Delete'
							};

							Ext.Ajax.request({
								params: params,
								url: 'stock',

								success: function(response) {
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
		}, {
			xtype: 'textfield',
			size: '8',
			labelAlign: 'right',
			fieldLabel: 'Total Quantity',
			name: 'sum',
			id: 'sum',
			readOnly: true
		}, {
			name: 'recount',
			id: 'recount',
			text: 'compute',
			handler: function() {

				sumTotal();

			}
		}, {
			xtype: 'hiddenfield',
			name: 'group',
			id: 'group',
			value: ''
		}, {
			xtype: 'hiddenfield',
			name: 'bal',
			id: 'bal',
			value: ''
		}]
	});

	//汇总
	function sumTotal() {

		var params = {
			fld1: cbfld1.getValue(),
			fld2: cbfld2.getValue(),
			cdt1: cbcdt1.getValue(),
			cdt2: cbcdt2.getValue(),
			val1: txtfld1.getValue(),
			val2: txtfld2.getValue(),
			sub: 'recount'
		};

		Ext.Ajax.request({
			params: params,
			url: 'stock',

			success: function(response) {
				var text = response.responseText;
				Ext.getCmp('sum').setValue(text)
			}

		});

	}

	function bindGroup() {
		Ext.Ajax.request({
			url: 'stock?sub=bindGroup',
			success: function(response) {
				Ext.getCmp('group').setValue(response.responseText);
			}
		});
	}

	//获取库存数量
	function getBalance() {

		var params = {
			'nbr': Ext.getCmp('items').getValue(),
			'sub': 'getBalance'
		};

		Ext.Ajax.request({
			params: params,
			url: 'stock',

			success: function(response) {
				Ext.getCmp('bal').setValue(response.responseText);
			}
		});

	}

	var tool = Ext.create('Ext.container.Container', {
		layout: {
			type: 'table',
			columns: 1
		},
		items: [findset, optset]
	})

	var tlb = Ext.create('Ext.toolbar.Toolbar', {
		width: 700,
		items: [tool]
	});

	var editing = Ext.create('Ext.grid.plugin.RowEditing', {
		autoCancel: false,
		clicksToEdit: 2
	});

	Ext.define('cbModel', {
		extend: 'Ext.data.Model',
		fields: ['val', 'desc']
	});

	//定义类型下拉列表
	var deptStore = new Ext.data.Store({
		model: 'cbModel',
		proxy: {
			type: 'ajax',
			url: 'stock?sub=bindDept',
			reader: {
				type: 'json',
				root: 'depts'
			}
		}
	});

	//定义类型下拉列表
	var currStore = new Ext.data.Store({
		model: 'cbModel',
		proxy: {
			type: 'ajax',
			url: 'stock?sub=bindCurr',
			reader: {
				type: 'json',
				root: 'currs'
			}
		}
	});


	Ext.define('itemModel', {
		extend: 'Ext.data.Model',
		fields: ['val', 'desc', 'cost', 'curr', 'vendor', 'um']
	});


	//定义类型下拉列表
	var itemStore = new Ext.data.Store({
		model: 'itemModel',
		proxy: {
			type: 'ajax',
			url: 'stock?sub=bindItem',
			reader: {
				type: 'json',
				root: 'items'
			}
		}
	});

	//定义类型下拉列表
	var descStore = new Ext.data.Store({
		model: 'cbModel',
		proxy: {
			type: 'ajax',
			url: 'stock?sub=bindDesc',
			reader: {
				type: 'json',
				root: 'descs'
			}
		}
	});
	var vendorStore = new Ext.data.Store({
		model: 'cbModel',
		proxy: {
			type: 'ajax',
			url: 'stock?sub=bindVendor',
			reader: {
				type: 'json',
				root: 'vendors'
			}
		}
	});

	//分页
	var pgt = new Ext.PagingToolbar({
		store: store,   // same store GridPanel is using
		dock: 'bottom',
		displayMsg: 'Data from {0} to {1} of {2}',
		displayInfo: true
	});


	store.on("beforeload", function() {
		newrecord = 0;

		params = {
			fld1: cbfld1.getValue(),
			fld2: cbfld2.getValue(),
			cdt1: cbcdt1.getValue(),
			cdt2: cbcdt2.getValue(),
			val1: txtfld1.getValue(),
			val2: txtfld2.getValue()
		};
		Ext.apply(store.proxy.extraParams, params);
	});

	//创建Grid
	var grid = Ext.create('Ext.grid.Panel', {
		title: 'Auxiliary materials in/out',
		store: Ext.data.StoreManager.lookup('stockStore'),
		columns: [
			{
				header: 'Department No.', dataIndex: 'dept',
				field: {
					xtype: 'combobox',
					id: 'depts',
					typeAhead: true,
					maxLength: 6,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					store: deptStore,
					lazyRender: true,
					displayField: 'desc',
					valueField: 'val',
					listeners: {
						select: function() {
							//仓库只允许输入正数，出库只能输入负数
							if (Ext.getCmp('depts').getValue() == 'STORE'
								|| Ext.getCmp('depts').getValue() == 'STOCK') {
								if (parseInt(Ext.getCmp('qty').getValue()) < 0)
									Ext.getCmp('qty').setValue(0 - parseInt(Ext.getCmp('qty').getValue()));
								const generatedLotNumber = generateLotNumber(store);
								Ext.getCmp('lot').setValue(generatedLotNumber);
							} else {
								if (parseInt(Ext.getCmp('qty').getValue()) > 0)
									Ext.getCmp('qty').setValue(0 - parseInt(Ext.getCmp('qty').getValue()));
								Ext.getCmp('lot').setValue('');
							}
						}
					}

				}
			},
			{
				header: 'Date', dataIndex: 'date', renderer: Ext.util.Format.dateRenderer('Y-m-d'),
				field: {
					xtype: 'datefield',
					allowBlank: false,
					format: "Y-m-d",
					submitFormat: "Y-m-d"
				}

			},
			{
				header: 'Item No.', dataIndex: 'nbr',
				field: {
					xtype: 'combobox',
					id: 'items',
					typeAhead: true,
					maxLength: 18,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					store: itemStore,
					lazyRender: true,
					displayField: 'desc',
					valueField: 'val',
					listeners: {
						select: function() {
							if (Ext.getCmp('items').getValue() != Ext.getCmp('descs').getValue()) {
								Ext.getCmp('descs').setValue(Ext.getCmp('items').getValue());
							}
							if (!(Ext.getCmp('depts').getValue() == 'STORE'
								&& Ext.getCmp('group').getValue() == 'PUR')) {

								var index = itemStore.find('val', Ext.getCmp('items').getValue());
								if (index != -1)
									/*									Ext.getCmp('cost').setValue(itemStore.getAt(index).data.cost);
																	Ext.getCmp('currs').setValue(itemStore.getAt(index).data.curr);*/
									Ext.getCmp('vendors').setValue(itemStore.getAt(index).data.vendor);
							}

							getBalance();

						}
					}
				}
			},
			{
				header: 'Item Name', dataIndex: 'desc',
				field: {
					xtype: 'combobox',
					id: 'descs',
					typeAhead: true,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					store: descStore,
					lazyRender: true,
					displayField: 'desc',
					valueField: 'val',
					listeners: {
						select: function() {
							if (Ext.getCmp('items').getValue() != Ext.getCmp('descs').getValue()) {
								Ext.getCmp('items').setValue(Ext.getCmp('descs').getValue());
							}

							if (!(Ext.getCmp('depts').getValue() == 'STORE'
								&& Ext.getCmp('group').getValue() == 'PUR')) {

								var index = itemStore.find('val', Ext.getCmp('items').getValue());
								if (index != -1)
									Ext.getCmp('cost').setValue(itemStore.getAt(index).data.cost);

							}

							getBalance();

						}
					}
				},

				renderer: function(value, metadata, record) {
					var index = descStore.find('val', value);
					if (index != -1) {
						return descStore.getAt(index).data.desc;
					}
					return value;
				}

			},
			{
				header: 'Quantity', dataIndex: 'qty',
				field: {
					xtype: 'numberfield',
					id: 'qty',
					listeners: {
						blur: function() {
							//仓库只允许输入正数，出库只能输入负数
							if (Ext.getCmp('depts').getValue() == 'STORE'
								|| Ext.getCmp('depts').getValue() == 'STOCK') {
								if (parseInt(Ext.getCmp('qty').getValue()) < 0)
									Ext.getCmp('qty').setValue(0 - parseInt(Ext.getCmp('qty').getValue()));
							} else {
								if (parseInt(Ext.getCmp('qty').getValue()) > 0)
									Ext.getCmp('qty').setValue(0 - parseInt(Ext.getCmp('qty').getValue()));
							}
						}
					}
				}
			},
			{
				header: 'PO', dataIndex: 'po',
				field: {
					id: 'po',
					maxValue: 8,
				}

			}, {
				header: 'Vendor',
				dataIndex: 'vendor',
				field: {
					xtype: 'combobox',
					id: 'vendors',
					typeAhead: true,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					store: vendorStore,
					lazyRender: true,
					displayField: 'val',
					valueField: 'val',
					listeners: {
						select: function() {
							if (!(Ext.getCmp('depts').getValue() == 'STORE'
								&& Ext.getCmp('group').getValue() == 'PUR')) {
								// var indexvendor = vendorStore.find('val', Ext.getCmp('vendors').getValue()); // Tìm vị trí của giá trị trong combobox "Vendor" trong store.
								var index = itemStore.find('val', Ext.getCmp('items').getValue());
								if (index !== -1) {
									console.log(Ext.getCmp('vendors').getValue());
									Ext.getCmp('um').setValue(itemStore.getAt(index).get('um')); // Set giá trị của combobox "Vendor" thành giá trị được chọn.
									Ext.getCmp('currs').setValue(itemStore.getAt(index).get('curr'));
									Ext.getCmp('cost').setValue(itemStore.getAt(index).get('cost'));
								}
							}
						}
					}
				}
			},

			{
				header: 'Currency', dataIndex: 'current',
				field: {
					xtype: 'combobox',
					id: 'currs',
					typeAhead: true,
					maxLength: 3,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					store: currStore,
					lazyRender: true,
					displayField: 'val',
					valueField: 'val'
				},
				renderer: function(value, metadata, record) {
					var index = currStore.find('val', value);
					if (index != -1)
						return currStore.getAt(index).data.val;
					return value;
				}

			},
			{
				header: 'Unit Price', dataIndex: 'cost',
				field: {
					xtype: 'numberfield',
					id: 'cost',
					minValue: 0
				}
			},
			{
				header: 'UM', dataIndex: 'um', maxLength: 30,
				field: {
					id: "um",
					xtype: 'textfield',
				}
			},
			{
				header: 'Vendor Lot', dataIndex: 'vendor_lot',
				field: {
					id: 'vendor_lot',
					maxValue: 48,
				}

			},
			{
				header: 'Expiry Date', dataIndex: 'expiry_date', renderer: Ext.util.Format.dateRenderer('Y-m-d'), field: {
					xtype: 'datefield',
					id: "expiry_date",
					allowBlank: true,
					format: "Y-m-d",
					submitFormat: "Y-m-d"
				}
			},
			{
				header: 'Remarks', dataIndex: 'rmk', maxLength: 30,
				field: {
					xtype: 'textfield',
					id: "rmk"
				}
			},
			{
				header: 'Lot No.', dataIndex: 'lot',
				field: {
					id: 'lot'
				}
			},
			{
				header: 'Printed',
				dataIndex: 'label_printed',
				field: {
					xtype: 'combobox',
					id: 'label_printed',
					typeAhead: true,
					maxLength: 3,
					triggerAction: 'all',
					queryMode: 'local',
					selectOnTab: true,
					lazyRender: true,
					displayField: 'desc',
					valueField: 'val',
					store: cbfld4
				}
			},
		],
		selType: 'rowmodel',
		plugins: [editing],
		height: Ext.getBody().getHeight() - 10,
		tbar: tlb,
		listeners: {
			selectionchange: function(model, records) {
				if (records[0]) {
					orgdept = records[0].data.dept;
					orgdate = records[0].data.date;
					orgnbr = records[0].data.nbr;

					//获取选中的行号
					row = grid.getStore().indexOf(records[0]);

					this.down('#delete').setDisabled(false);
				} else {
					this.down('#delete').setDisabled(true);
				}
			},

			itemdblclick: function() {
				Ext.getCmp('descs').setValue(Ext.getCmp('items').getValue());
				getBalance();
			}
		},
		dockedItems: [pgt],
		renderTo: Ext.getBody()
	});
	function formatDateToYYYYMMDD(date) {
		const day = date.getDate();
		const month = date.getMonth() + 1;
		const year = date.getFullYear();
		const formattedDay = day.toString().padStart(2, '0');
		const formattedMonth = month.toString().padStart(2, '0');
		return `${year}${formattedMonth}${formattedDay}`;
	}

	function generateLotNumber(store) {
		const currentDate = new Date();
		const dateAsString = formatDateToYYYYMMDD(currentDate);
		const lots = [];
		const lastThreeCharacters = [];

		for (let i = 0; i < store.getCount(); i++) {
			const record = store.getAt(i);
			const lotValue = record.get('lot');
			if (lotValue !== "") {

				var lotNumCheck = lotValue.slice(0, 8);
				if (lotNumCheck === dateAsString) {
					lots.push(lotNumCheck);
					if (lotValue.length >= 3) {
						const lastThree = lotValue.slice(-3);
						lastThreeCharacters.push(lastThree);
					}
				}
			}
		}
		var maxNumber = 1;
		console.log(maxNumber);
		for (let i = 0; i < lots.length; i++) {
			if (lots[i] === dateAsString) {
				maxNumber = Math.max(...lastThreeCharacters.map(Number)) + 1;
			}
		}
		const formattedNumber = maxNumber.toString().padStart(3, '0');
		const lotNumber = dateAsString + formattedNumber;
		return lotNumber; // return lotNumber
	}

	function onEdit(e) {

		//检查记录是否存在,不存在就提交    	
		var params = {
			'orgdept': orgdept,
			'orgdate': orgdate,
			'orgnbr': orgnbr,
			'dept': e.record.data.dept,
			'date': e.record.data.date,
			'nbr': e.record.data.nbr,
			'qty': e.record.data.qty,
			'po': e.record.data.po,
			'vendor': e.record.data.vendor,
			'current': e.record.data.current,
			'cost': e.record.data.cost,
			'um': e.record.data.um,
			'vendorlot': e.record.data.vendor_lot,
			'expirydate': e.record.data.expiry_date,
			'rmk': e.record.data.rmk,
			'lot': e.record.data.lot,
			'labelprinted': e.record.data.label_printed,
			'sub': 'Save'
		};

		if ((parseInt(Ext.getCmp('qty').getValue()) < 0)
			&& (parseInt(Ext.getCmp('bal').getValue()) + parseInt(Ext.getCmp('qty').getValue()) < 0)) {
			Ext.MessageBox.confirm('out of stock', 'Current inventory ' + Ext.getCmp('bal').getValue() + ',Continue?', function(id) {
				if (id == 'yes') {
					Ext.Ajax.request({
						params: params,
						url: 'stock',
						success: function(response) {

							var text = response.responseText;

							if (text == 'exists') {
								Ext.MessageBox.alert("exists", "Record already exists, please check.");
								e.record.reject();
								editing.startEdit(row, 0);
							} else if (text == 'ok') {
								Ext.MessageBox.alert("succeeded", "Record writing succeeded");
								//记录写入成功后可修改
								orgdept = e.record.data.dept;
								orgdate = e.record.data.date;
								orgnbr = e.record.data.nbr;

								e.record.commit();
								if (row == 0 && newrecord > 0)
									newrecord = 0;
							} else if (text == 'failed') {
								Ext.MessageBox.alert("failed", "Record writing failed");
								e.record.reject();
							}
						}
					});

				} else {
					e.record.reject();
				}
			})
		} else {
			Ext.Ajax.request({
				params: params,
				url: 'stock',
				success: function(response) {
					var text = response.responseText;
					if (text == 'exists') {
						Ext.MessageBox.alert("exists", "Record already exists, please check.");
						e.record.reject();
						editing.startEdit(row, 0);
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

	}

	grid.on('edit', onEdit, this);

	bindGroup();
	deptStore.load();
	itemStore.load();
	descStore.load();
	currStore.load();
	vendorStore.load();
	store.load();

});