let checkHistory = (userIdx) => {
	console.dir(userIdx)
   location.href = `/member/mentor-info?user_idx=${userIdx}`;
}

let apply = (mentorUserIdx, mentorIdx) => {
	location.href = `/mentoring/apply-complete?mentor_user_idx=${mentorUserIdx}&mentor_idx=${mentorIdx}`;
}


