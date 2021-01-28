$(document).ready(function(){
  $('#submitDeletionButton').click(function() {
    $.ajax({
      type: 'DELETE',
      data : {
        "gymId" : $(this).attr('data-id')
      },
      url: '/admin/delete-gym',
      success: function() {
        location.reload();
      },
      cache:false
    });
  });

  $("[id^=deleteButton_]").click(function() {
    transferId($(this));
  });

  $("#addGymButton").click(function() {
    $("#addGymForm").submit();
  });
});