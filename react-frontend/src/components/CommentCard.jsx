import formattedDate from "../helper/formattedDate"
import "../styles/CommentCard.css"
import { Link } from "react-router-dom";
import { FaBackspace, FaTimes, FaEdit } from "react-icons/fa";
import { useEffect, useState } from "react";
import ConfirmationModal from "./ConfirmationModal";
import { BASE_URL } from "../helper/connection";
import axios from "axios";
import { deleteComment } from "../helper/deleteFunctions";
import { Typography, TextField, Button } from "@mui/material"




const CommentCard = (props) => {
	const [me, setMe] = props.me
	const comment = props.comment;
	const post = comment.post;
	const [content, setContent] = useState(comment.content);
	const createDate = comment.createDate;
	const authorName = (comment.author ? comment.author.username : undefined)
	const [showCommentDelete, setShowCommentDelete] = useState(false);
    const [showCommentEdit, setShowCommentEdit] = useState(false);
	const [showModal, setShowModal] = useState(false);
	const [commentsEndpoint, setComments] = props.fetchComments;
	const [onEdit, setOnEdit] = useState(false);
	const [newCommentContent, setNewCommentContent] = useState(content);

	const config = {
        headers: {
            Authorization: `Bearer ${me}`
        },
    }

	useEffect(() => {
		if (me) {
            const userEndpoint = BASE_URL + "users/auth";
            const config = {
                headers: {
                    Authorization: `Bearer ${me}`
                },
            }
            axios.get(userEndpoint, config)
                .then((res) => {
                    (res.data.role === "ADMIN" || (res.data.username === authorName) ? setShowCommentDelete(true) : setShowCommentDelete(false))
                    console.log(`Get user (${res.data.username}) from bearer token`);
                    (res.data.username === authorName ? setShowCommentEdit(true) : setShowCommentEdit(false))
					if (res.data.username === post.author.username) {setShowCommentDelete(true)}
                })
                .catch((err) => {
                    console.log(err);
                })
        } else {
            setShowCommentDelete(false);
            setShowCommentEdit(false)
        }
	}, [me])

	const handleDeleteComment = () => {
        setShowModal(true);
    }


	const confirmDelete = async() => {
		await deleteComment(comment.id, post.id, config);
		axios.get(commentsEndpoint)
			.then((res) => {
				setComments(res.data);
				console.log("Comment fetched");
			})
			.catch((err) => {
				console.error(err);
			})
		setShowModal(false);
	}

	const cancelDelete = () => {
        setShowModal(false);
    };

	const submitEdit =() => {
		const updateEndPoint = BASE_URL + `posts/${post.id}/comments/${comment.id}/update`
		const updatedComment = {
            "content": newCommentContent
        }

		axios.put(updateEndPoint, updatedComment, config)
            .then((res) => {
                console.log("Updated Comment");
				setContent(newCommentContent);
            })
            .catch((err) => {
                console.err(err)
            })
		setOnEdit(false);
	} 


	return (
		<div className="comment-card">
			<div className="icon-group">
				{showCommentEdit && !onEdit
					? <FaEdit className="icon edit-icon" onClick={() => setOnEdit(!onEdit)}/>
					: showCommentEdit && onEdit ? <FaBackspace className="icon edit-icon" onClick={() => setOnEdit(!onEdit)}/>
					: <></>
				} 
				{showCommentDelete && !onEdit
					? <FaTimes className="icon delete-icon" onClick={handleDeleteComment} /> 
					: <></>	
				}  
			</div>
			{(onEdit) 
				? <div>
					<TextField
							className="edit-field"
							fullWidth
							multiline
							label="Change Comment"
							value={newCommentContent}
							onChange={(e) => setNewCommentContent(e.target.value)}
						/>
					<p></p>
					<Button className="confirm-edit" 
							variant="contained" 
							onClick={submitEdit}
					>Edit</Button>
				</div>
				: <p className="comment-content">{content}</p> }
			<p className="comment-author">Comment by: 
				{(authorName !== undefined) 
					? <Link className="author-link" to={`/user/${authorName}`}>
							{authorName}        
						</Link>                                                      
					: <span className="deleted-user">Deleted User</span>}
			</p>
			<p className="comment-date">Commented on: {formattedDate(createDate)}</p>
			{showModal && (
                <ConfirmationModal 
                    message={`Are you sure you want to delete this comment?`}
                    onConfirm={confirmDelete}
                    onCancel={cancelDelete}/>
            )}
		</div>
	);
};

export default CommentCard
