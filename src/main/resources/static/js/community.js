/**
 * 提交回复
 */

function postComment() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    commentToTarget(questionId, 1, content);
}

//封装
function commentToTarget(targetId, type, content) {
    if (!content) {
        alert("输入的内容不能为空哦！");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            //JSON.stringify() 可以将对象转换为字符串
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                // $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message); // windows 自带的一个弹出框
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=4ebf5af562037b19de87&redirect_uri=http://localhost:8081/callback&scope=user&state=1")
                        // html5 web 存储： 在本地存储用户的浏览数据，但不会保存在服务器上，只用于用户请求网站数据上
                        // localStorage：长久保存整个网站的数据，没有过期时间，直到手动去除
                        // sessionStorage：临时保存同一窗口(或标签页)的数据，在关闭窗口或标签页之后将会删除这些数据
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    commentToTarget(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComment(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    // 获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 打开状态，那么再次点击则 折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var secCommentContainer = $("#comment-" + id);
        if (secCommentContainer.children().length != 1) {
            // 展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            //获取请求
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 sec-content sec-content-sp"
                    }).append(mediaElement);

                    secCommentContainer.prepend(commentElement);
                });
                // 展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}