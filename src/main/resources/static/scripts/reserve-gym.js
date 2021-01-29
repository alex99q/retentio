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

  $("#gym").change(function() {
    alert('nice');
  });

  $("#editGymButton").click(function() {
      $("#editGymForm").submit();
  });

});