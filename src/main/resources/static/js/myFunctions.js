var principal_id = null;
$(document).ready(function(){
    $.ajax({
      type:"GET",
      url:"http://"+location.host+"/principal_id",

      success: function(data) {
        principal_id = data;
      },
      error: function(data) {
        console.log(data);
      }
    });

    $.ajax({
      type:"GET",
      contentType: "application/json",
      url:"http://"+location.host+"/api/posts",
      success: function(data) {
        var offerFound = false;
        var postFound = false;
        var posts = JSON.parse(JSON.stringify(data));
        var access;
        var myNewPostHtml = null;
            for (var i in posts) {
                access = "disabled";
                if(posts[i].userId == principal_id){access = ""};
                myNewPostHtml = newPostHtml(posts[i].id, posts[i].mydescription, posts[i].commentsNumber, posts[i].firstName, posts[i].familyName, access, posts[i].like, posts[i].likesNumber);
                if(posts[i].isOffer){
                    if(!offerFound){ $("#offerArea").empty(); offerFound = true }
                    $(myNewPostHtml).appendTo("#offerArea").show("slow");
                }else{
                    if(!postFound){ $("#postArea").empty(); postFound = true }
                    $(myNewPostHtml).appendTo("#postArea").show("slow")
                }
          }
          if(!postFound){
                setTimeout(function(){ $("#postArea").find(".loading").text("No user shared any POST yet, take the initiative ;)") }, 1500);
           }
           if(!offerFound){
                setTimeout(function(){ $("#offerArea").find(".loading").text("No user has shared any Offer yet!") }, 1500);
           }
      },
      error: function(data) {
        console.log(data);
        }
    });

  $('#postModal').on('show.bs.modal', function(event) {
          var button = $(event.relatedTarget);
          var recipient = button.data('whatever');
          var modal = $(this);
          $.ajax({
                type:"GET",
                contentType: "application/json",
                url:"http://"+location.host+"/api/posts/"+recipient,
                success: function(data) {
                    var post = JSON.parse(JSON.stringify(data));
                    modal.find('.modal-body #postUpdate').val(post.mydescription);
                    modal.find('.modal-body #postUpdateId').val(post.id);
                },
                error: function(data) {
                 console.log(data);
                    modal.find('.modal-body .form-group').remove();
                    modal.find('#postModalLabel').text('Not found!');
                }
          });

      });

  $("#usersArea").empty();
        $.ajax({
          type:"GET",
          contentType: "application/json",
          url:"http://"+location.host+"/admin/users",
          success: function(data) {
            var users = JSON.parse(JSON.stringify(data));
            var myNewUserHtml = null;
                for (var i in users) {
                myNewUserHtml = newUserHtml(users[i].id, users[i].firstName, users[i].familyName, users[i].username, users[i].role, users[i].enabled);
                    $(myNewUserHtml).appendTo("#usersArea").show("slow");
                }
          },
          error: function(data) {
            console.log(data);
            }
        });

});

function editComment(element){
        $("#editCommentArea").remove();
        var commentTr = element.closest("tr");
        var commentContentTd = commentTr.querySelector("td .commentContentTd");
        var htmlString = '<tr id="editCommentArea" style="display:none"><td colspan="4"><form onsubmit="sendUpdatedComment(event,'+commentTr.id+')">'+
                              '<div class="input-group mb-0">'+
                                  '<input type="text" value="'+commentContentTd.textContent+'" class="form-control" placeholder="Type your comment" aria-describedby="basic-addon2">'+
                                  '<div class="input-group-append">'+
                                      '<button class="btn btn-dark" type="submit">Save</button>'+
                                  '</div>'+
                                  '<div class="input-group-append">'+
                                      '<button class="btn btn-warning" onclick="cancelEditComment(this)" type="button">Cancel</button>'+
                                  '</div>'+
                              '</div>'+
                          '</form></td></tr>';
        $(htmlString).insertAfter(commentTr).show("slow");
}


function cancelEditComment(element){
        var commentEditTr = element.closest("tr");
        $(commentEditTr).remove();
}


function fetchComments(event, postId){
      var commentsDivArea = '#commentsDivArea'+postId;
      var commentArea = '#commentArea'+postId;
      var collapseComments = '#collapse'+postId;
      var access = "none";
    if($(event.currentTarget).is(':checked')){
        $.ajax({
          type:"GET",
          contentType: "application/json",
          url:"http://"+location.host+"/api/posts/"+postId+"/comments",
          success: function(data) {
            var comments = JSON.parse(JSON.stringify(data));
              for (var i in comments) {
                  if(comments[i].user_id == principal_id){ access="" };
                  var commentHtml = newCommentHtml(comments[i].post_id, comments[i].id,comments[i].comment, comments[i].creationdate, comments[i].firstName, comments[i].familyName, access);
                  $(commentHtml).appendTo(commentArea).show();
                  $(commentArea).show("fast");
                  access = "none";
            }
          },
          error: function(data) {
            console.log(data);
            }
        });
        setTimeout(function(){
        $(collapseComments).collapse("show");
        }, 200);
    }else{
           $(collapseComments).collapse("hide");
            setTimeout(function(){
            $(commentArea).empty();
            }, 500);
     }
};

var stringToColour = function(str) {
    var hash = 0;
    for (var i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    var colour = '#';
    for (var i = 0; i < 3; i++) {
        var value = (hash >> (i * 8)) & 0xFF;
        colour += ('00' + value.toString(16)).substr(-2);
    }
    return colour;
};

function coloredUsername(firstName, familyName){
    var color = stringToColour(firstName+familyName);
    var usernameHtml = '<span style="background-color:'+color+'" class="dot">'+familyName.charAt(0).toLocaleUpperCase()+'</span>'+familyName.substr(1)+' '+firstName;
    return usernameHtml;
}
function coloredAvatar(firstName, familyName){
    var color = stringToColour(firstName+familyName);
    var userAvatarHtml = '<span style="background-color:'+color+'" class="dot">'+familyName.charAt(0).toLocaleUpperCase()+'</span>';
    return userAvatarHtml;
}

function deleteUserTrigger(event){
//        console.log(event.currentTarget);
        var mytarget = event.currentTarget;
        bootstrapConfirmation({
            yesCallBack: function() {
                deleteUser(mytarget);
            },
            noCallBack: function() {
                console.log('no');
            },
            config: {
                closeIcon: true,
                message: 'Do you really want ot delete this account ?',
                title: 'Delete',
                no: {
                    class: 'btn btn-danger',
                    text: 'No'
                },
                yes: {
                    class: 'btn btn-success',
                    text: 'Yes'
                }
            }
        });
}
function enableUserTrigger(event){
        var mytarget = event.currentTarget;
        bootstrapConfirmation({
            yesCallBack: function() {
                enableUser(mytarget);
            },
            noCallBack: function() {
                console.log('no');
            },
            config: {
                closeIcon: true,
                message: 'Are you sure you want to Disable/Enable this account ?',
                title: 'Enable/Disable',
                no: {
                    class: 'btn btn-danger',
                    text: 'No'
                },
                yes: {
                    class: 'btn btn-success',
                    text: 'Yes'
                }
            }
        });
}

function deleteUser(element){

    console.log(element);
    var buttonDiv = element.closest("div");
    var userDiv = buttonDiv.parentElement;
    var username = buttonDiv.querySelector(".uname");
    $.ajax({
        type:"DELETE",
        url:"http://"+location.host+"/api/users/" + username.value,
        success: function(data){
            $(userDiv).hide("slow", function() {$(this).remove()})
        },
        error: function(err) {
            console.log(err);
            alert(err);
        }
    });

}

function enableUser(element){
    var myTargetButton = element;
    var buttonColor = "#343a40";
    var buttonTextColor = "#f8f9fa";

    var accountTag = myTargetButton.closest("div").parentElement.querySelector(".enabled");
    var username = myTargetButton.closest("div").querySelector(".uname");
    var enableButtonText = "Disable";
    var accountText = "Enabled";

    if(myTargetButton.textContent == "Disable"){
        enableButtonText = "Enable";
        accountText = "Disabled";
        buttonColor = "#f8f9fa";
        buttonTextColor = "#343a40";
    }

    var myJson = {enabled:accountText};

    $.ajax({
        type:"POST",
        url:"http://"+location.host+"/api/users/"+username.value,
        data: JSON.stringify(myJson),
        contentType: "application/json",
        processData:false,
        success: function(){
            myTargetButton.textContent = enableButtonText;
            myTargetButton.style.backgroundColor = buttonColor;
            myTargetButton.style.color = buttonTextColor;
            myTargetButton.style.borderColor = buttonColor;
            accountTag.innerText = accountText;
        },
        error: function(err) {
            console.log(err);
        }
    });
}

function newUserHtml(user_id, firstName,familyName, username, role, enabled){
    var userAvatarHtml = coloredAvatar(firstName,familyName);
    var buttonColor = "dark";
    var enabledText = "Enabled";
    var enableButtonText = "Disable";
    if(!enabled){
        enabledText = "Disabled";
        enableButtonText = "Enable";
        buttonColor = "light";
    }
    var userHtml =     '<div class="posts row">'+
                           '<div class="col-lg-1 postUser">'+
                               '<p>'+userAvatarHtml+'</p>'+
                           '</div>'+
                           '<div class="col-lg-2 postUser">'+
                               '<p class="attributeName">First name</p>'+
                               '<p>'+firstName+'</p>'+
                           '</div>'+
                           '<div class="col-lg-2 postUser">'+
                               '<p class="attributeName">Last name</p>'+
                               '<p>'+familyName+'</p>'+
                           '</div>'+
                           '<div class="col-lg-2 postUser">'+
                               '<p class="attributeName">Username</p>'+
                               '<p>'+username+'</p>'+
                           '</div>'+
                           '<div class="col-lg-2 postUser">'+
                               '<p class="attributeName">Account</p>'+
                               '<p class="enabled">'+enabledText+'</p>'+
                           '</div>'+
                           '<div class="col-lg-2 postUser">'+
                               '<p class="attributeName">Role</p>'+
                               '<p>'+role+'</p>'+
                           '</div>'+
                           '<div class="likeComment col-lg-1">'+

                               '<button class="btn btn-'+buttonColor+' btn-block" onclick="enableUserTrigger(event)">'+enableButtonText+'</button>'+
                               '<button class="btn btn-danger btn-block" onclick="deleteUserTrigger(event)">Delete</button>'+
                               '<a class="btn btn-info btn-block" role="button">Edit</a>'+
                               '<input type="hidden" class="uname" value="'+username+'">'+
                           '</div>'+
                        '</div>';
                    return userHtml;
}
function newPostHtml(post_id, post_description, comments_Number, firstName, familyName, access, likeChecked, likes_Number){
    var usernameHtml = coloredUsername(firstName,familyName);
    var checkedString = null;
    if(likeChecked){
        checkedString = "checked";
    }else{
        checkedString = "unchecked";
    }
        var postHtml = '<div class="postAndComments" style="display:none;" id="postAndComments'+post_id+'">'+
                            '<div class="posts row">'+
                                '<div class="threedots btn-group dropleft">'+
                                    '<button type="button" class="threedotsbutton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
                                        '<span>&#10247;</span>'+
                                    '</button>'+
                                    '<div class="dropdown-menu">'+
                                        '<button class="dropdown-item" type="button" data-toggle="modal" data-target="#postModal" data-whatever="'+post_id+'" '+access+'>Edit</button>'+
                                        '<button class="dropdown-item" type="button" onclick="sendDeletedPost(this)" value="'+post_id+'" '+access+'>Delete</button>'+
                                    '</div>'+
                                '</div>'+
                                '<div class="col-lg-6 postText"><p>'+post_description+'</p></div>'+
                                '<div class="col-lg-3 postUser">'+usernameHtml+'</div>'+
                                '<div class="likeComment col-lg-3">'+

                                    '<input type="checkbox" class="likeInputCheckbox" onclick="sendLike(event,'+post_id+')" id="like_check_button'+post_id+'" role="button" '+checkedString+'>'+
                                    '<label class="btn btn-light btn-block likeLabelCheckbox" for="like_check_button'+post_id+'"><p id="likeNumberLabel'+post_id+'" style="display:inline">'+likes_Number+'</p><i class="icon-emo-thumbsup"></i></label>'+

                                    '<input type="checkbox" class="btn-block commentInputCheckbox" onclick="fetchComments(event,'+post_id+')" id="comment_check_button'+post_id+'" role="button">'+
                                    '<label class="btn btn-warning btn-block commentLabelCheckbox" for="comment_check_button'+post_id+'"><p id="commentNumberLabel'+post_id+'" style="display:inline">'+comments_Number+'</p><i class="icon-comment"></i></label>'+
                                    '<input type="hidden" id="commentNumberHidden'+post_id+'"  value="'+comments_Number+'" >'+
                                    '<input type="hidden" id="likeNumberHidden'+post_id+'"  value="'+likes_Number+'" >'+
                                '</div>'+
                            '</div>'+
                            '<div class="collapse" id="collapse'+post_id+'">'+
                                '<div class="tableCommentsDiv">'+
                                    '<div>'+
                                        '<form onsubmit="return sendComment(event,'+post_id+')">'+
                                            '<div class="input-group mb-0">'+

                                                '<input id="comment'+post_id+'" type="text" class="form-control" placeholder="Type your comment" autocomplete="off" aria-describedby="basic-addon2">'+
                                                '<div class="input-group-append">'+
                                                    '<button class="btn btn-dark" type="submit">Share</button>'+
                                                '</div>'+
                                            '</div>'+
                                        '</form>'+
                                    '</div>'+
                                    '<div id="commentsDivArea'+post_id+'">'+
                                        '<table class="mytableComments table table-dark table-sm"><tbody id="commentArea'+ post_id+'"></tbody></table>'+
                                    '</div>'+
                                '</div>'+
                            '</div>'+
                        '</div>';
        return postHtml;
}

function newCommentHtml(postId,commentId, commentContent, creationdate, firstName, familyName, access){
    var usernameHtml = coloredUsername(firstName, familyName);
    var time = new Date(creationdate);

    var hours = toTwoDigits(time.getHours());
    var minutes = toTwoDigits(time.getMinutes());

    var style = 'style="display:'+access+'"';
    var commentHtml = '<tr class="comment" style="display: none" id="'+commentId+'">'+
                                '<th class="commentUserTd">'+usernameHtml+'</th>'+
                                '<td><p class="commentContentTd">'+commentContent+'</p></td>'+
                                '<td class="dateTd">'+hours+":"+minutes+'</td>'+
                                '<td>'+
                                    '<div class="btn-group dropleft">'+
                                        '<button type="button" class="threedotsComment" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
                                            '<span>&#10247;</span>'+
                                        '</button>'+
                                        '<div '+style+' class="dropdown-menu">'+
                                            '<button class="dropdown-item" onclick="editComment(this)" type="button" autocomplete="off">Edit</button>'+
                                            '<button class="dropdown-item" onclick="sendDeletedComment(this)" type="button" autocomplete="off">Delete</button>'+
                                            '<input type="hidden" value="'+postId+'">'
                                        '</div>'+
                                    '</div>'+
                                '</td>'+
                        '</tr>';
    return commentHtml;
}
function toTwoDigits(number){
    number = number.toString();
    if(number.length < 2){
        number = "0" + number ;
    }
    return number;
}