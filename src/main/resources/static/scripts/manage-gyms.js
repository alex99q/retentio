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

  $("[id^=editButton_]").click(function() {
    transferId($(this));
    $.ajax({
          type: 'GET',
          url: '/admin/view-gym/' + $(this).attr('data-id'),
          success: function(response) {
            $('#editGymContainer').html(response);
          },
          cache:false
        });
  });

  $("#addGymButton").click(function() {
    $("#addGymForm").submit();
  });

  $("#editGymButton").click(function() {
      $("#editGymForm").submit();
  });

});