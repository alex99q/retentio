function transferId(initiatingTransfer) {
  let id = $(initiatingTransfer).attr('data-id');
  $('.transfer-id').attr('data-id', id);
}

$(document).ready(function(){
  $('.datepicker').datepicker();

  $("#tableSearch").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#searchTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
