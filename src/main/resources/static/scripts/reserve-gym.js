function reservationTableAjax() {
  $.ajax({
    type: 'GET',
    url: '/user/ajax-reservation-table/' +  $('#gym').val() + '/' + $('#date').val().replace(/\//g, "-"),
    success: function(response) {
      $('#reservationTableContainer').html(response);
    },
    cache:false
  });
}

$(document).ready(function(){
  reservationTableAjax();

  $("#gym").change(function() {
    reservationTableAjax();
  });

  $("#date[type='text']").change( function() {
    reservationTableAjax();
  });
});