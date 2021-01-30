$(document).ready(function(){
  $('#submitDeletionButton').click(function() {
      $.ajax({
        type: 'DELETE',
        data : {
          "reservationId" : $(this).attr('data-id')
        },
        url: '/user/delete-reservation',
        success: function(message) {
          location.reload();
          if (message !== "") {
            alert(message);
          }
        },
        cache:false
      });
    });

    $("[id^=deleteButton_]").click(function() {
      transferId($(this));
    });
});