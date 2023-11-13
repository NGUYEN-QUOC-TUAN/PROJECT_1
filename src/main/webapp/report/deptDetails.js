$(document).ready(function(){
	$("<h1/>").text("Department Auxiliary Materials Cost Details").css('text-align',"center").appendTo($("body"));
	$("<div/>").css("text-align","center").attr({'id':'messageDiv'}).appendTo($("body"));
	var headDiv = $("<form/>").css('text-align','center');
	headDiv.attr({"id" : "queryForm"});
	headDiv.append("Start Time");
	$("<input/>").attr({'id':'startDate','type': 'text','name':'startDate'}).on("click", WdatePicker).appendTo(headDiv);
	headDiv.append("End Time");
	$("<input/>").attr({'id':'endDate','type': 'text','name':'endDate'}).on("click", WdatePicker).appendTo(headDiv);
	headDiv.appendTo($('body'));
	$("<button/>").attr({'type': 'submit'}).text("confirm").appendTo(headDiv);
	$("<div/>").attr("align","center").append($("<button/>").attr({'type': 'text'}).text("export").on("click",output)).appendTo($("body"));
	
	var hiddenForm=$("<form/>");//定义一个form表单
	hiddenForm.attr("style","display:none");
	hiddenForm.attr("target"," _blank");
	hiddenForm.attr("method","post");
	hiddenForm.attr("action",ctx+'/deptDetails!output');
	var hiddenInput=$("<input>");
	hiddenInput.attr("type","hidden");
	hiddenInput.attr("name","table");
	hiddenInput.attr("value",(new Date()).getMilliseconds());
	$("body").append(hiddenForm);//将表单放置在web中
	hiddenForm.append(hiddenInput);
	var queryStatus = false;
	function output(){
		if(queryStatus){
			hiddenInput.attr("value",$("table").prop("outerHTML"));
			hiddenForm.submit();//表单提交
		}else{
			alert("please select a date and confirm.");
		}
	}
	//`cat_desc`, `item_num`, `item_desc`, `item_std`, `item_cost`, `tx_dept`,"
	// " `tx_date`, `tx_qty`, `tx_curr`, `tx_cost`, `tx_rmk`, `dept_desc`
	function getList(){
		var params = {
			startDate :$("#startDate").val(),
			endDate :$("#endDate").val()
		};
		
		$.getJSON( ctx + "/deptDetails!getList",params, function(data){
			$('table').remove();
			getTable(data.rows);
		});
		queryStatus = true;
	}
	
	function stringToHex(str){
		var val="";
		for(var i = 0; i < str.length; i++){
			if(val == "")
				val = str.charCodeAt(i).toString(16);
			else
				val += "" + str.charCodeAt(i).toString(16);
		}
		return val;
	}
	
	var rowGroupArray = ["tx_curr","dept_desc","cat_desc","item_desc"];
	var colGroupArray = ["tx_date"];
	var colArray;
	
	function getTable(rowArray){
		var table = $("<table/>");
		table.attr({"border":1, cellpadding : "5" ,cellspacing:"0"});
		table.css('margin','auto');
		table.appendTo($("body"));
		colArray = getColGroup(rowArray).sort();
		var headTr = $("<tr/>");
		if(colGroupArray.length > 0){
			headTr.append($("<td/>").attr("colSpan",rowGroupArray.length));
			$.each(colArray , function(index , col){
				headTr.append($("<td/>").text(col));
			});
			headTr.appendTo(table);
		}
		var added = {};
		var totalObject = {};
		$.each(rowArray , function(ondeIn,node){
			var rowGroupParent;
			var sumTr = $("<tr/>");
			var qtyTr = $("<tr/>");
			
			//判断是否应该被删除
			var lastVal = getValue(rowGroupArray.length -1 , node);
			if(added[lastVal]){
				var remove = true;
			}else{
				table.append(qtyTr);
				table.append(sumTr);
			}
			$.each(rowGroupArray , function(colIndex , rowGroupValue){
				var val = getValue(colIndex , node);
				if(!added[val] && colIndex < rowGroupArray.length -1){
					totalObject[val] = {index : colIndex};
					for(var colIn = 0 , colLast = colArray.length ; colIn <colLast ; colIn++){
						if(colArray[colIn] == node[colGroupArray[0]].substring(0,7)){
							totalObject[val][colArray[colIn]] = {sum : (node["tx_cost"]*1*node["tx_qty"]).toFixed(2),qty : (node["tx_qty"]*1).toFixed(2)}; 
						}else{
							totalObject[val][colArray[colIn]] = {sum : 0,qty :0}; 
						}
					}
				}
				if(added[val] && colIndex < rowGroupArray.length -1){
					for(var colIn = 0 , colLast = colArray.length ; colIn <colLast ; colIn++){
						if(colArray[colIn] == node[colGroupArray[0]].substring(0,7)){
							totalObject[val][colArray[colIn]] = {sum : (totalObject[val][colArray[colIn]].sum*1 + node["tx_cost"]*1*node["tx_qty"]).toFixed(2),qty : (totalObject[val][colArray[colIn]].qty*1 + node["tx_qty"]*1).toFixed(2)}; 
						}
					}
				}
				
				if(!remove){
					if(val != null){
						var valelem = $("#" + val);
						if(valelem.length > 0 && !remove){
							valelem.css("border-bottom",0).removeAttr("id");
							qtyTr.append($("<td/>").attr({"id" : val,rowSpan:2}).html("&nbsp;").css("border-top",0));
						}else{
							qtyTr.append($("<td/>").attr({"id" : val,rowSpan:2}).text(node[rowGroupValue]));
						}
					}else{
						qtyTr.append($("<td/>").html("&nbsp;"));
					}
					added[val] = true;
				}
			});
			if(colGroupArray.length > 0){
				$.each(colArray , function(colIndex , col){
					var val = getValue(rowGroupArray.length , node,node[colGroupArray[0]].substring(0,7));
					var colVal = getValue(rowGroupArray.length , node , col);
					var qtyVal = getValue(rowGroupArray.length , node , col + "qty");
					var valelem = $("#" + val + "");
					if(val == colVal && valelem.length > 0){
						valelem.text(( $.text(valelem)*1 + (node["tx_cost"]*1*node["tx_qty"])).toFixed(2));
						var qtyelem = $("#" + qtyVal + "");
						qtyelem.text(( $.text(qtyelem)*1 + node["tx_qty"]*1).toFixed(2))
					}
					if(!remove){
						var colTd = $("<td/>").attr({"id" : colVal}).html("&nbsp;").css("border-top",0);
						var qtyTd = $("<td/>").attr({"id" : qtyVal}).html("&nbsp;").css("border-bottom",0);
						if(col == node[colGroupArray[0]].substring(0,7) ){
							colTd.text((node["tx_cost"]*1*node["tx_qty"]).toFixed(2));
							qtyTd.text((node["tx_qty"]*1).toFixed(2));
						}
						sumTr.append(colTd);
						qtyTr.append(qtyTd);
					}
				});
			}
		});
		getTotal(totalObject);
		getColTotal();
	}
	
	function getTotal(totalObject){
		$.each(totalObject , function(key , val){
			var sumTotalTr = $("<tr/>");
			var qtyTotalTr = $("<tr/>");
			$("#" + key).parent().next().after(qtyTotalTr);
			qtyTotalTr.after(sumTotalTr);
			//$("#" +　key).css("border-bottom",0);
			for(var index = 0 ;index < val.index ; index ++){
				qtyTotalTr.append($("<td/>").html("&nbsp;").attr({rowSpan : 2}).css("border-top",0).css("border-bottom",0));
			}
			qtyTotalTr.append($("<td/>").attr({colSpan : rowGroupArray.length - val.index ,rowSpan : 2}).text("Total"));
			for(index = 0 ;index < colArray.length ; index ++){
				sumTotalTr.append($("<td/>").text(val[colArray[index]].sum).css("border-top",0));
				qtyTotalTr.append($("<td/>").text(val[colArray[index]].qty).css("border-bottom",0));
			}
		});
	}
	
	function getColTotal(){
		$("tr").each(function(index , tr){
			if(index == 0){
				$(tr).append($("<td/>").text("Total"));
			}else{
				var sum = 0;
				var tds = $(tr).find("td");
				if(tds.length > colArray.length){
					tdIn = 0;
				}else{
					
				}
				for(var tdIn = 0 ; tdIn < colArray.length ;tdIn ++){
					var tdVal = $(tds[tds.length-1 - tdIn]).text()*1;
					if(isNaN(tdVal)){
						tdVal = 0;
					}
					sum = (sum*1 + tdVal*1);
					sum = (sum*1).toFixed(2);
				}
				$(tr).append($("<td/>").text(sum));
			}
		});
	}
	
	var getValue = function(index , node , key){
		var groupVal = "";
		for(var grIn = 0 ; grIn <= index ; grIn ++){
			if(node[rowGroupArray[grIn]] == ""){
				node[rowGroupArray[grIn]] = "null";
			}
			groupVal = groupVal + node[rowGroupArray[grIn]];
		}
		if(key){
			return stringToHex(groupVal + key);
		}
		return stringToHex(groupVal);
	}
	
	var getInt = function(string){
		var val = parseInt(string);
		if(isNaN(val)){
			return 0;
		}else{
			return val;
		}
	}
	
	function getColGroup(dataArray){
		var colGroup = new Array();
		if(colGroupArray.length > 0){
			$.each(colGroupArray , function(index , node){
				if(colGroupArray[index] == 'tx_date'){
					$.each(dataArray , function(dataIndex , dataNode ){
						var eq = true;
						$.each(colGroup , function(colIndex , colValue){
							if(dataNode[colGroupArray[index]].substring(0,7) == colValue){
								eq = false;
								return false;
							}
						});
						if(eq){
							colGroup.push(dataNode[colGroupArray[index]].substring(0,7))
						}
					});
				}
			});
		}
		return colGroup;
	}
	
	$('#queryForm').validate({
		submitHandler:function(form){
			getList();
        },
		rules: {
			startDate: "required",
			endDate: "required",
			materiel: {
				required: true,
				digits: true
			}
		},
		messages: {
			startDate: "please enter the start time.",
			endDate: "please enter the end time.",
			materiel: {
				required: "please enter the starting Part NO.",
				digits: "Part NO. is an integer."
			}
		},
		errorLabelContainer: "#messageDiv",
		wrapper:'li'
	});
});

function getDateTime() {

    var data = new Date();  
    var year = data.getFullYear();  //获取年
    var month = data.getMonth() + 1;    //获取月
    var day = data.getDate(); //获取日
    var hours = data.getHours(); 
    var minutes = data.getMinutes();
    time = year + "year" + month + "month" + day + "day" + " " + hours + ":" + minutes + ":" + data.getSeconds();
    return time;
}
