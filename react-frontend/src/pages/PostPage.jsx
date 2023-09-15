import { useParams } from "react-router-dom";
import { BASE_URL } from "../helper/connection";
import { useEffect, useState } from "react";
import { Typography, TextField, Button } from "@mui/material"
import axios from "axios";
import useAxios from "../helper/useAxios"
import CommentCard from "../components/CommentCard";
import PostCard from "../components/PostCard";
import "../styles/PostPage.css"
import useTokenGetUser from "../helper/useTokenGetUser";




const PostPage = (props) => {
    const [me, setMe] = props.me;
    const [myUser, setMyUser] = useTokenGetUser(me);
    const { postId } = useParams();
    const endpoint = BASE_URL + "posts/search?id=" + postId;
    const commentEndpoint = BASE_URL + `posts/${postId}/comments`
    const [errorMsg, setErrorMsg] = useState("");
    const [post, setPost] = useState([]);
    const [comments, setComments] = useAxios([], commentEndpoint, setErrorMsg);
    const [showPostDelete, setShowPostDelete] = useState(false);
    const [showPostEdit, setShowPostEdit] = useState(false);
    const [newCommentContent, setNewCommentContent] = useState();

    useEffect(() => {
        axios.get(endpoint)
            .then((res) => {
                setPost(res.data[0])
                if (myUser.username === res.data[0].author.username) {
                    setShowPostDelete(true)
                    setShowPostEdit(true)
                } else if (myUser.role === "ADMIN") {
                    setShowPostDelete(true)
                } else {
                    setShowPostDelete(false)
                    setShowPostEdit(false)
                }
                console.log("Retrieved Post")
            })
            .catch((err) => {
                console.error(err)
            })
    }, [myUser, me]);

    const handleAddComment = () => {
        const createCommentEndpoint = BASE_URL + `posts/${postId}/comments/create`;
        const newComment = {
            "content": newCommentContent,
            "createDate": new Date().toISOString()
        }
        const config = {
            headers: {
                Authorization: `Bearer ${me}`
            },
        }
        axios.post(createCommentEndpoint, newComment, config)
            .then((res) => {
                console.log("Comment added");
            })
            .then(() => {
                axios.get(commentEndpoint)
                    .then((res) => {
                        setComments(res.data);
                        console.log("Updated comments");
                        setNewCommentContent("");
                    })
                    .catch((err) => {
                        setErrorMsg("Fail to update comments");
                    })
            })
            .catch((err) => {
                setErrorMsg("Fail to add comment");
            })
    };

    return (
        <div className="post-page">
            <PostCard 
                post={post}
                me={[me, setMe]}
                showIcons={[showPostDelete, showPostEdit]}
                fetchPosts={[undefined, undefined]}
            />
            <div className="post-content-wrapper">
                <h2>Content:</h2>
                <p className="post-content">{post.content}</p>
            </div>

            <div className="comments">
                <h2 className="comments-header">Comments</h2>
                <div className="comment-list">
                {comments.map((comment) => (
                    <CommentCard
                    key={comment.id}
                    me={[me, setMe]}
                    comment={comment}
                    fetchComments={[commentEndpoint, setComments]}
                    />
                ))}
                </div>  
            </div>

            {(me) ? <>
                    <div className="add-comment">
                        <TextField
                            fullWidth
                            multiline
                            label="Add a Comment"
                            value={newCommentContent}
                            onChange={(e) => setNewCommentContent(e.target.value)}
                        />
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleAddComment}
                            style={{ marginTop: "10px" }}
                        >
                            Add Comment
                        </Button>
                    </div>
                    </>
                    : <></>}
            <Typography style={{color: "red"}}>{errorMsg}</Typography>
        </div>
    )
}

export default PostPage
