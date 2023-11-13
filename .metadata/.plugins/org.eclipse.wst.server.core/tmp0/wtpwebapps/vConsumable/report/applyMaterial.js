$(document).ready(function(){
	//$().message("Hello world, on document ready!");
	$("<h1/>").text("New Work Clothes Report").css('text-align',"center").appendTo($("body"));
	$("<div/>").css("text-align","center").attr({'id':'messageDiv'}).appendTo($("body"));
	var headDiv = $("<form/>").css('text-align','center');
	headDiv.attr({"id" : "queryForm"});
	headDiv.append("Start Time");
	$("<input/>").attr({'id':'startDate','type': 'text','name':'startDate'}).on("click", WdatePicker).appendTo(headDiv);
	headDiv.append("End Time");
	$("<input/>").attr({'id':'endDate','type': 'text','name':'endDate'}).on("click", WdatePicker).appendTo(headDiv);
	headDiv.append("Part NO.");
	$("<input/>").attr({'id':'materiel','type': 'text','name':'materiel'}).val("10").appendTo(headDiv);
	headDiv.appendTo($('body'));
	$("<button/>").attr({'type': 'submit'}).text("confirm").appendTo(headDiv);
	$("<div/>").attr("align","center").append($("<button/>").attr({'type': 'text'}).text("export").on("click",output)).appendTo($("body"));
	
	var hiddenForm=$("<form/>");//定义一个form表单
	hiddenForm.attr("style","display:none");
	hiddenForm.attr("target"," _blank");
	hiddenForm.attr("method","post");
	hiddenForm.attr("action",ctx+'/applyMaterial!output');
	var hiddenInput=$("<input/>");
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
	
	function getList(){
		$('table').remove();
		
		var params = {
			startDate :$("#startDate").val(),
			endDate :$("#endDate").val(),
			materiel :$("#materiel").val()
		};
		
		var rowArray = [
			{index : 'cat_desc',head : 'subsidiary material'},
			{index : 'tx_curr',head : 'currency'},
			{index : 'dept_desc',head : 'department'},
			{index :'sum',head : 'amount'}
		];
		var table;
		if($("table").length < 1){
			table = createTable(rowArray);
			table.attr({"border":1, cellpadding : "5" ,cellspacing:"0"});
			table.css('margin','auto');
			table.appendTo($("body"));
		}
		$.getJSON( ctx + "/applyMaterial!getList",params, function(data){
			$.each(data.rows, function(index,row){
				var dataTr = $("<tr/>").attr("dataTr",'data' + index);
				dataTr.appendTo(table);
				$.each(rowArray, function(colindex,rowIndex){
					$("<td/>").text(row[rowIndex.index]).attr({"wid": index + rowIndex.index }).appendTo(dataTr);
				});
				queryStatus = true;
			});
			
			var groupString = cookieUtils.get('group');
			groupString = groupString.replace(/\"/g,"");
			var group = groupString.split(",");
			$.each(group ,function(index , groupField){
				var groupList = new Array();
				//groupListObject = {group:'',rowSpan:'',index}
				var value;
				var rowSpanInt = 0;
				var rowIndex = 0;
				var groupElem = $("[wid*=" + groupField + "]"); 
				groupElem.each( function(index , rowValue){
					if(value == null){
						value = $(rowValue).text();
					}
					if(value == $(rowValue).text()){
						if(rowIndex == 0){
							rowIndex = index;
						}
						rowSpanInt = rowSpanInt + 1 ;
					}else{
						value = $(rowValue).text();
						if(rowIndex != 0){
							var groupListObject = {group:groupField ,rowSpan:rowSpanInt , index : rowIndex};
							groupList.push(groupListObject);
						}
						rowIndex = 0;
						rowSpanInt = 0;
					}
					if(index == groupElem.length - 1){
						if(value == $(rowValue).text()){
							if(rowIndex == 0){
								rowIndex = index;
							}
							rowSpanInt = rowSpanInt + 1 ;
							if(rowIndex != 0){
								var groupListObject = {group:groupField ,rowSpan:rowSpanInt , index : rowIndex};
								groupList.push(groupListObject);
							}
						}else{
							rowIndex = 0;
							rowSpanInt = 0;
						}
					}
				});
				$.each(groupList ,function(index , groupElem){
					$("[wid=" + (groupElem.index -1) + groupElem.group + "]").attr("rowSpan" ,groupElem.rowSpan).attr({ALIGN : 'CENTER', VALIGN:"TOP"});
					for(var i = 0 ; i <groupElem.rowSpan -1; i++){
						$("[wid=" + (groupElem.index + i) + groupElem.group + "]").remove();
					}
				})
			});
			
		});
	}
	
	function createTable(rowArray){
		var table = $("<table/>");
		var headTr = $("<tr/>");
		headTr.appendTo(table);
		$.each(rowArray, function(index,col){
			$("<th/>").text(col.head).appendTo(headTr);
		});
		return table;
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
				required: "Please enter the starting Part NO.",
				digits: "Part NO. is an integer."
			}
		},
		errorLabelContainer: "#messageDiv",
		wrapper:'li'
	});
});