$(document).ready(function(){
  $('#submitDeletionButton').click(function() {
      $.ajax({
        type: 'DELETE',
        data : {
          "reservationId" : $(this).attr('data-id')
        },
        url: '/admin/delete-reservation',
        success: function() {
          location.reload();
        },
        cache:false
      });
    });

    $("[id^=deleteButton_]").click(function() {
      transferId($(this));
    });

  $("#addReservationButton").click(function() {
    $("#addReservationForm").submit();
  });
});