function transferId(initiatingTransfer) {
  let id = $(initiatingTransfer).attr('data-id');
  $('.transfer-id').attr('data-id', id);
}

$(document).ready(function(){
  $('.datepicker').datepicker();

  $('.timepicker').timepicker({
      timeFormat: 'HH:mm',
      interval: 30,
      minTime: '10',
      maxTime: '19:00',
      defaultTime: '11',
      startTime: '10:00',
      dynamic: false,
      dropdown: true,
      scrollbar: true
  });

  $("#tableSearch").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#searchTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});