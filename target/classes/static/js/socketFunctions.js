'use strict';


var postForm = document.querySelector('#postForm');
var postInput = document.querySelector('#post');
var postUpdateForm = document.querySelector('#postUpdateForm');
var postUpdateInput = document.querySelector('#postUpdate');
var postUpdateId = document.querySelector('#postUpdateId');
var offerForm = document.querySelector('#offerForm');
var offerInput = document.querySelector('#offer');

var messageElements = document.querySelectorAll('.message');
var stompClient = null;

function connect(event) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/post', onPostReceived);
    stompClient.subscribe('/topic/comment', onCommentReceived);
    stompClient.subscribe('/topic/like', onLikeReceived);
    stompClient.subscribe('/topic/updatedComment', onCommentUpdatedReceived);
    stompClient.subscribe('/topic/updatedPost', onPostUpdatedReceived);
    stompClient.subscribe('/topic/deletedPost', onPostDeletedReceived);
    stompClient.subscribe('/topic/deletedComment', onCommentDeletedReceived);
}


function onError(error) {
    var i;
    for (i = 0; i < messageElements.length; ++i) {
        messageElements[i].textContent = 'Something went wrong, Please refresh the page!';
        messageElements[i].style.color = 'red';
    }
}


function sendPost(event) {
    $("#addPostArea").collapse('hide');
    var postContent = postInput.value.trim();
    if(postContent && stompClient) {
        var postObj = {
            mydescription: postInput.value,
            isOffer: 0
        };
        stompClient.send("/app/sendPost", {}, JSON.stringify(postObj));
        postInput.value = '';
    }
    event.preventDefault();
}

function sendOffer(event) {
    $("#addOfferArea").collapse('hide');
    var offerContent = offerInput.value.trim();
    if(offerContent && stompClient) {
        var offerObj = {
            mydescription: offerInput.value,
            isOffer: 1
        };
        stompClient.send("/app/sendPost", {}, JSON.stringify(offerObj));
        offerInput.value = '';
    }
    event.preventDefault();
}

function sendUpdatedPost(event) {
    event.preventDefault();
    $('#postModal').modal('hide');
    var postUpdateContent = postUpdateInput.value.trim();
    if(postUpdateContent && stompClient) {
        var postUpdateObj = {
            mydescription: postUpdateInput.value,
            id: postUpdateId.value
        };
        stompClient.send("/app/sendUpdatedPost", {}, JSON.stringify(postUpdateObj));
        postUpdateInput.value = '';
        postUpdateId.value ='';
    }
}

function sendDeletedPost(element){
        if(stompClient) {
            var postId = "" + element.value;
            var postDeleteId = postId.toString();
            stompClient.send("/app/sendDeletedPost", {}, postDeleteId);
        }
}

function sendDeletedComment(element){
    if(stompClient) {
                var commentTrId = element.closest("tr").id;
                var commentId = "" + commentTrId;
                var commentDeleteId = commentId.toString();
                stompClient.send("/app/sendDeletedComment", {}, commentDeleteId);
    }
}

function onCommentDeletedReceived(payload){
    var commentDeleteId = payload.body;
    var postId = $("#"+commentDeleteId).find("input[type=hidden]").val();
    var tagId = '#'+commentDeleteId;
    var commentNumberLabel = "#commentNumberLabel"+postId;
    var commentNumberHidden = "#commentNumberHidden"+postId;
    var newCommentsNumber = Number($(commentNumberHidden).val()) - 1;
    $(commentNumberHidden).val(newCommentsNumber);
    $(commentNumberLabel).text(newCommentsNumber);
    $(tagId).hide("slow", function() {$(this).remove()});
}

function sendComment(event, id) {
    var commentInput = document.querySelector('#comment'+id);
    var commentContent = commentInput.value.trim();
    if(commentContent && stompClient) {
        var commentObj = {
            comment: commentInput.value,
            post_id: id
        };
        stompClient.send("/app/sendComment", {}, JSON.stringify(commentObj));
        commentInput.value = '';
    }
    event.preventDefault();
}

function sendLike(event, id){
    var likeObj = {
            post_id: id,
            like: true
        };
    var button = event.currentTarget;
    if($(button).is(':checked')){
        if(stompClient) {
            stompClient.send("/app/sendLike", {}, JSON.stringify(likeObj));
        }
    }else{
        likeObj = {
            post_id: id,
            like: false
          };
        if(stompClient) {
            stompClient.send("/app/sendDislike", {}, JSON.stringify(likeObj));
        }
    }
}

function onPostReceived(payload) {
    var access = "disabled";
    var post = JSON.parse(payload.body);
    if(post.userId == principal_id){access = ""};
    var myNewPostHtml = newPostHtml(post.id, post.mydescription, post.commentsNumber, post.firstName, post.familyName, access, post.like, post.likesNumber);
    setTimeout(function(){
        if(post.isOffer){
            if ($("#offerArea .posts:first .loading").length){
                $("#offerArea").empty();
            }
            $(myNewPostHtml).prependTo("#offerArea").show("slow");
        }else{
            if ($("#postArea .posts:first .loading").length){
                $("#postArea").empty();
            }
            $(myNewPostHtml).prependTo("#postArea").show('slow');
        }
        }, 500);
}

function onPostUpdatedReceived(payload){
    var postUpdate = JSON.parse(payload.body);
    $('#postAndComments'+postUpdate.id + ' .postText p').text(postUpdate.mydescription);
}
function onCommentUpdatedReceived(payload){
    var commentUpdate = JSON.parse(payload.body);

    $('#commentArea'+commentUpdate.post_id + " #"+commentUpdate.id + " .commentContentTd").text(commentUpdate.comment);
}

function onPostDeletedReceived(payload){
    var postDeleteId = payload.body;
    var stringHtml;
    var tagId = '#postAndComments'+postDeleteId;
    $(tagId).hide("slow", function() {$(this).remove()});
    setTimeout(function(){
        if ($("#postArea").is(":empty")){
            stringHtml = '<div style="display:none" class="postAndComments"><div class="posts row"><h3 class="loading">No user has shared any POST yet</h3></div></div>';
            $(stringHtml).appendTo("#postArea").show("slow");
        }
        if ($("#offerArea").is(":empty")){
            stringHtml = '<div style="display:none" class="postAndComments"><div class="posts row"><h3 class="loading">No user has shared any OFFER yet</h3></div></div>';
            $(stringHtml).appendTo("#offerArea").show("slow");
        }
     }, 1000);
}

function onCommentReceived(payload) {
    var comment = JSON.parse(payload.body);
    var access = "none";
    var commentNumberLabel = "#commentNumberLabel"+comment.post_id;
    var commentNumberHidden = "#commentNumberHidden"+comment.post_id;
    var newCommentsNumber = Number($(commentNumberHidden).val()) + 1;
    $(commentNumberHidden).val(newCommentsNumber);
    $(commentNumberLabel).text(newCommentsNumber);

    if($('#comment_check_button'+comment.post_id).is(':checked')){
        if ( comment.user_id == principal_id){ access = ""};
        var myCommentHtml = newCommentHtml(comment.post_id,comment.id,comment.comment, comment.creationdate, comment.firstName, comment.familyName, access);
        $(myCommentHtml).prependTo("#commentArea" + comment.post_id).show("slow");
    }
}

function onLikeReceived(payload) {
    var like = JSON.parse(payload.body);
    var likeNumberLabel = "#likeNumberLabel"+like.post_id;
    var likeNumberHidden = "#likeNumberHidden"+like.post_id;
    var newLikesNumber;
    if(like.like){
        newLikesNumber = Number($(likeNumberHidden).val()) + 1;
    }else{
        newLikesNumber = Number($(likeNumberHidden).val()) - 1;
    }
    $(likeNumberHidden).val(newLikesNumber);
    $(likeNumberLabel).text(newLikesNumber);
}

function sendUpdatedComment(event, id) {
    var formTarget = event.currentTarget;
    var commentInput = $(formTarget).find("input").val();
    var commentContent = commentInput.trim();
    if(commentContent && stompClient) {
        var commentObj = {
            comment: commentInput,
            id: id
        };
        stompClient.send("/app/sendUpdatedComment", {}, JSON.stringify(commentObj));
    }
    $("#editCommentArea").remove();
    event.preventDefault();
}

//window.addEventListener('load', connect, true);
$(window).on('load', function(event){ connect(event) })
//postForm.addEventListener('submit', sendPost, true);
$(postForm).on('submit', function(event){ sendPost(event) })
//offerForm.addEventListener('submit', sendOffer, true);
$(offerForm).on('submit', function(event){ sendOffer(event)})
//postUpdateForm.addEventListener('submit', sendUpdatedPost, true);
$(postUpdateForm).on('submit', function(event){ sendUpdatedPost(event)})