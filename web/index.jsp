<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试Filter和文件上传</title>
    <style>
        a {
            color: #13CE66;
            text-decoration: none;
        }

        .button {
            display: inline-block;
            padding: 5px 10px;
            font-size: 16px;
            background-color: #34db42;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }

        /* 鼠标悬停时的样式 */
        .button:hover {
            background-color: #2980b9;
        }

        /* 按钮点击时的样式 */
        .button:active {
            background-color: #1c4966;
        }

        #reminder {
            display: none;
            position: fixed;
            top: 10px;
            left: 50%;
            transform: translateX(-50%);
            background-color: #f2f2f2;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }


    </style>
</head>
<body>
<h1>Filter</h1>
<p>admin下的三个文件 需要登才能访问</p>
<hr>
<a href="javascript:void(0)" onclick="skipWebpage('admin/1.html')">好看输入框</a>
<a href="javascript:void(0)" onclick="skipWebpage('admin/2.html')">彩色的雨</a>
<a href="javascript:void(0)" onclick="skipWebpage('admin/3.html')">各种提示按钮</a><br><br>
<button class="button" onclick="login()">登录</button>
<button class="button" onclick="logout()" style="background-color: #db3498;">注销</button>

<h1>Upload</h1>
<hr>
<div>
    <form action="ImgServlet?method=submitImgByForm" method="post" enctype="multipart/form-data">
        <input type="file" id="myFileInput" name="file" onchange="showImage()"><br>
        <img src="" alt="" id="showImg" width="100px" style="display: none"><br>
        <input class="button" type="submit" value="Form提交">
    </form>
    <button style="background-color: red" class="button" id="submitByAjax" onclick="submitByAjax()">
        异步提交
    </button>
</div>
<hr>
<div>
    <button class="button" id="showAllImg" onclick="showAllImg()">显示所有图片</button><br>
    <div id="showImgDiv"></div>
</div>
<!-- 提醒框 -->
<div id="reminder">这是一个提醒！</div>
</body>
</html>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

    function showAllImg() {
        let $showAllImg = $("#showImgDiv");
        $showAllImg.html("");
        $.post(
            "ImgServlet",
            "method=showAllImg",
            function (msg){
                for (let i = 0; i < msg.length; i++) {
                    let imgPath = msg[i];
                    console.log(imgPath)
                    $showAllImg.append(
                        '<img src="'+imgPath+'" alt="" width="100px">'
                    );


                }
            }
        );
    }

    function submitByAjax() {
        let $myFileInput = $("#myFileInput");
        let file = $myFileInput.prop("files")[0];

        if (file) {
            let formData = new FormData();
            formData.append("file", file);

            console.log(formData)

            $.ajax({
                type: "post",
                url: "ImgServlet?method=submitImgByAjax",
                data: formData,
                processData: false,  // 不要处理数据
                contentType: false,  // 不要设置内容类型
                success: function (msg) {
                    showReminder("上传成功!" + msg)
                },
                error: function () {
                    alert("失败");
                }
            });
        }
    }

    function showImage() {
        //抓取到上传图片的input标签的信息
        let file_input = document.getElementById("myFileInput");
        //抓取到需要展示预览图的img标签信息
        let show_img = document.getElementById("showImg");

        //回去预览图的src属性信息
        show_img.src = window.URL.createObjectURL(file_input.files[0]);
        //改变style属性中block的值
        show_img.style.display = 'block';

    }

    function login() {
        $.post(
            "UserServlet",
            "method=userLogin",
            function (msg) {
                if (msg) {
                    showReminder(msg);
                }
            }
        )
    }

    function logout() {
        $.post(
            "UserServlet",
            "method=userLogout",
            function (msg) {
                if (msg) {
                    showReminder(msg);
                }
            }
        )
    }

    //网页跳转
    function skipWebpage(address) {
        $.post(
            address,
            "",
            function (msg) {
                if (msg !== "请登录~") {
                    window.location.href = address;
                } else {
                    showReminder(msg);
                }
            }
        );
    }

    //提示框
    function showReminder(world) {
        let reminder = document.getElementById('reminder');
        reminder.innerHTML = world;

        // 显示提醒框
        reminder.style.display = 'block';

        // 一秒后自动关闭提醒框
        setTimeout(() => {
            reminder.style.display = 'none';
        }, 3000);
    }
</script>
