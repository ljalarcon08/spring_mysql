$(document).ready(function() {
	var win = $(window);
	var doc = $(document);
	// Each time the user scrolls
	win.scroll(function() {
		// Vertical end reached?
		if (doc.height() - win.height() == win.scrollTop()) {
			// New row
			
			
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

function getContextPath() {
	   console.log(window.location.pathname);
	   console.log(window.location.pathname.replace("/list",""));
	   return window.location.pathname.replace("/list","");
	}

function putData(page,rowCount){
	var initial_path=getContextPath();
	var host=window.location.host;
	$.get( 'http://'+host+initial_path+"jsonPage?pageSize=5&page="+page, function( data ) {
		//console.log(data);
		if(page<=data.totalPages&&rowCount<data.totalElements){
			var n_cols = $('#table tr:first-child th').length;
			for(var i in data.content){
				//var tr = $('<tr />').append($('<th />')).appendTo($('#table'));
				var tr = $('<tr />').appendTo($('#table'));
				tr.append('<td>'+data.content[i].movieid+'</td>');
				tr.append('<td>'+data.content[i].title+'</td>');
				tr.append('<td>'+data.content[i].release_year+'</td>');
				tr.append('<td>'+data.content[i].rating+'</td>');
				tr.append('<td><img src="'+data.content[i].image+'"/></td>');
				tr.append('<td><a class="btn btn-primary" href="/test/edit?movieid='+data.content[i].movieid+'">Actualizar</a></td>');
			    tr.append('<td><form method="post" action="/test/del?movieid='+data.content[i].movieid+'"><input class="btn btn-danger" type="button" value="Eliminar" onclick="eliminar(this)"></input><input type="hidden" name="_method" value="DELETE"></input></form></td>');
			}
			//for (var i = 0; i < n_cols; ++i)
				//tr.append($('<td />'));
		}
	});
}