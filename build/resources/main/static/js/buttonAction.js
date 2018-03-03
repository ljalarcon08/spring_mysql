$(document).ready(function(){

	$("#addButton").click(function(){
		var movieid=$("#movieid").val();
		var host=window.location.host;
		var rute=window.location.pathname.replace("/add","")
		var title=$("#title").val();
		var release_year=$("#release_year").val();
		var rating=$("#rating").val();
		var image=$("#image").val();
		var jsonRequest={
				movieid:movieid,
				title:title,
				release_year:release_year,
				rating:rating,
				image:image
		};
		$.ajax({
			headers:{
				'Accept':'application/json',
				'Content-type':'application/json'
			},
			'type':'POST',
			'url':'http://'+host+rute+'/api/movies',
			'data':JSON.stringify(jsonRequest),
			'dataType':'json',
			'statusCode': {
				201:function(xhr){
					window.location.href ="http://"+host+rute+"/list";
				}
			}
		});
	});

	$("#updateButton").click(function(){
		var movieid=$("#movieid").val();
		var host=window.location.host;
		var rute=window.location.pathname.replace("/edit","")
		var title=$("#title").val();
		var release_year=$("#release_year").val();
		var rating=$("#rating").val();
		var image=$("#image").val();
		var jsonRequest={
				movieid:movieid,
				title:title,
				release_year:release_year,
				rating:rating,
				image:image
		};
		$.ajax({
			headers:{
				'Accept':'application/json',
				'Content-type':'application/json'
			},
			'type':'PUT',
			'url':'http://'+host+rute+'/api/movies/'+movieid,
			'data':JSON.stringify(jsonRequest),
			'dataType':'json',
			'statusCode': {
				201:function(xhr){
					window.location.href ="http://"+host+rute+"/list";
				}
			}
		});
	});	
	
});