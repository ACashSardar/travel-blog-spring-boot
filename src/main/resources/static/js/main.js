let inputImg = document.querySelector("#inputImg")
let img = document.querySelector("#displayImg")
let likeBtnList = document.getElementsByClassName("btn like-btn")
let likeCountList = document.getElementsByClassName("like-count")
let comment = document.getElementById("comment")
let cancelBtn = document.getElementById("cancel-comment-btn")
let homepageImage = document.getElementById("homepage-image")
let todaysDate=document.getElementById("todays-date")

if (homepageImage != null) {
	const imageArray = [
		"https://www.thetimes.co.uk/travel/wp-content/uploads/sites/6/2022/01/USE_Pagoda-overlooking-Mount-Fuji-Japan_Credit_GettyImages-520571022.jpg",
		"https://img.freepik.com/premium-photo/view-paris-from-arc-de-triomphe-paris-france_501530-4742.jpg?w=2000",
		"https://media.cntraveler.com/photos/5b9aab2ee1b6e96e7b9f4e11/16:9/w_2560%2Cc_limit/Switzerland_Bernina_GettyImages-626077868.jpg",
	]

	let i = 0;

	setInterval(() => {
		i++;
		i = i % (imageArray.length);
		homepageImage.src = imageArray[i]
	}, 2500)
}

if(todaysDate!==null){
	let date = new Date().toUTCString();
	todaysDate.innerHTML=date.slice(0, 16)
}


if (inputImg !== null) {
	inputImg.addEventListener("change", function() {
		const file = this.files[0]
		if (file) {
			const reader = new FileReader();
			reader.onload = function() {
				const result = reader.result;
				img.src = result;
			};
			reader.readAsDataURL(file);
		}
	});
}

let baseURL = "https://travel-blog-spring-boot-production.up.railway.app";
// let baseURL = "http://localhost:8080";
let deleteURL = "";
let accentColor = "#0dcaf0";

function confirmDelete(element) {
	deleteURL = element.name;
	console.log(element);
	console.log(deleteURL);
}

function deleteEntity() {
	fetch(baseURL + deleteURL).then(() => {
		if (deleteURL.includes("/users/delete/") || deleteURL.includes("/categories/delete/") || deleteURL.includes("/posts/delete2/")) {
			baseURL += "/dashboard/admin";
		}
		else if (deleteURL.includes("/comments/delete/")) {
			baseURL += "/posts/" + deleteURL.split("/")[4];
		}
		window.location.href = baseURL;
	}).catch(() => console.log("an error occured"))
}

Array.from(likeBtnList).forEach(element => {
	const info = element.name.split("/")
	element.style.border = "1px solid " + accentColor;

	if (info[2] === "false") {
		element.style.background = "";
		element.style.color = accentColor;
		element.innerHTML = "Like <i class='fa fa-thumbs-up'></i>"
	}
	else {
		element.style.background = accentColor;
		element.style.color = "white";
		element.innerHTML = "Liked <i class='fa fa-thumbs-up'></i>"
	}
})

Array.from(likeCountList).forEach(e => console.log(e.innerHTML))


function reactpost(element) {
	console.log(element.name, " Liked")
	const targetIndex = Array.from(likeBtnList).indexOf(element)
	if (element.style.background == "") {
		element.style.background = accentColor;
		element.style.color = "white"
		element.innerHTML = "Liked <i class='fa fa-thumbs-up'></i>"
		likeCountList[targetIndex].innerHTML = parseInt(likeCountList[targetIndex].innerHTML) + 1
	}
	else {
		element.style.background = "";
		element.style.color = accentColor;
		element.innerHTML = "Like <i class='fa fa-thumbs-up'></i>"
		likeCountList[targetIndex].innerHTML = parseInt(likeCountList[targetIndex].innerHTML) - 1
	}
	fetch(baseURL + "/posts/like/" + element.name.replace("/false", "").replace("/true", "")).then(() => {
		console.log("request sent to: " + baseURL + "/posts/like/" + element.name.replace("/false", "").replace("/true", ""))
	});
}

function clearComment(element) {
	comment.value = "";
}