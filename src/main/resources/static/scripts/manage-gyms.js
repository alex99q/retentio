$(document).ready(function(){
  $('#submitDeletionButton').click(function() {
    $.ajax({
      type: 'DELETE',
      data : {
        "gymId" : $(this).attr('data-id')
      },
      url: '/admin/deleteGym',
      success: function() {
        location.reload();
      },
      cache:false
    });
  });

  $("[id^=deleteButton_]").click(function() {
    transferId($(this));
  });
});

function submitAddGym() {
  document.getElementById("addGymForm").submit();
}