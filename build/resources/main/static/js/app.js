$(document).ready(function() {
	var win = $(window);
	var doc = $(document);
	// Each time the user scrolls
	win.scroll(function() {
		// Vertical end reached?
		if($("#wait").length==0){
			if (doc.height() - win.height() == win.scrollTop()) {
				// New row
				fired=true;
				
				//console.log($( "#pageSizeSelect option:selected" ).text());
				var countSize=5;
				var rowCount = $('#table tr').length;
				//console.log("REAL ROW COUNT:"+rowCount);
				rowCount-=1;
				//console.log("ROW:"+rowCount);
				var page=(rowCount/countSize)+1|0;
				//console.log("PAGE ROUND"+page);
				putData(page,rowCount);
				// Current number of columns to create
			}
		}
		// Horizontal end reached?
		/*
		if (doc.width() - win.width() == win.scrollLeft()) {
			// New column in the heading row
			$('#spreadsheet tr:first-child').append($('<th />'));

			// New column in each row
			$('#spreadsheet tr:not(:first-child)').each(function() {
				$(this).append($('<td />'));
			});
		}*/
		
	});
	
});

$(document).on('click','.deleteButton',function(e){
	if(confirm("Seguro de elminiar")){
		var movieid=$(this).attr("id");
		var host=window.location.host;
		var rute=window.location.pathname.replace("/list","")
		$.ajax({
			headers:{
				'Accept':'application/json',
				'Content-type':'application/json'
			},
			'type':'DELETE',
			'url':'http://'+host+rute+'/api/movies/'+movieid,
			'dataType':'json',
			'statusCode': {
				204:function(xhr){
					window.location.href ="http://"+host+rute+"/list";
				}
			}
		});
	}		
});

function getContextPath() {
	   return window.location.pathname.replace("/list","");
	}

function putData(page,rowCount){
	var initial_path=getContextPath();
	var host=window.location.host;
	var urlComplete='http://'+host;
	if(initial_path.includes("findTitle")){
		//console.log(window.location.href);
		var actualUrl=new URL(window.location.href);
		initial_path=initial_path.replace("findTitle","");
		urlComplete+=initial_path+"/movies/"+actualUrl.searchParams.get("title");
		//console.log(urlComplete);
	}
	else{
		urlComplete+=initial_path+"/movies";
	}
	urlComplete+="?pageSize=5&page="+page;
	var tr = $('<tr />').appendTo($('#table'));
	tr.append('<td id="wait" colspan="7"><i id="spinner" class="fas fa-spinner fa-spin"></i></td>');
	$.get( urlComplete, function( data ) {
		//console.log(data);
		if(page<=data.totalPages&&rowCount<data.totalElements){
			var n_cols = $('#table tr:first-child th').length;
			var tr = $('<tr />').appendTo($('#table'));
			for(var i in data.content){
				//var tr = $('<tr />').append($('<th />')).appendTo($('#table'));
				tr = $('<tr />').appendTo($('#table'));
				tr.append('<td>'+data.content[i].movieid+'</td>');
				tr.append('<td>'+data.content[i].title+'</td>');
				tr.append('<td>'+data.content[i].release_year+'</td>');
				tr.append('<td>'+data.content[i].rating+'</td>');
				tr.append('<td><img src="'+data.content[i].image+'"/></td>');
				tr.append('<td><a class="btn btn-primary" href="/edit?movieid='+data.content[i].movieid+'">Actualizar</a></td>');
			    tr.append('<td><form id="delete_form"><input type="button" id="'+data.content[i].movieid+'" class="btn btn-danger buttonsp deleteButton" value="Eliminar"></input></form></td>');
			}
			//for (var i = 0; i < n_cols; ++i)
				//tr.append($('<td />'));
			$("#wait").remove();	
		}
		else{
			$("#wait").remove();
		}
		
	});
}