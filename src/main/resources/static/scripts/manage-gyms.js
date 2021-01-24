$(document).ready(function(){
  $('#submitDeletionButton').click(function() {
    $.ajax({
      type: 'DELETE',
      data: function (dataId) {
        dataId = $(this).attr('data-id');
      },
      url: '/admin/deleteGym',
      cache:false
    });
  });

  $("[id^=deleteButton_]").click(function() {
    transferId($(this));
  });

});