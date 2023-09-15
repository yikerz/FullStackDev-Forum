import { useEffect, useState } from "react";
import { BASE_URL } from "../helper/connection";
import axios from "axios";
import formattedDate from "../helper/formattedDate"
import "../styles/PostCard.css"
import { Link } from "react-router-dom";
import { FaTimes, FaEdit } from "react-icons/fa";
import ConfirmationModal from "./ConfirmationModal";
import { deletePost } from "../helper/deleteFunctions";
import { useNavigate } from "react-router-dom";


const PostCard = (props) => {
    const [me, setMe] = props.me;
    const [showDelete, showEdit] = props.showIcons;
    const [postsEndpoint, setPosts] = props.fetchPosts;
    const post = props.post;
    const postId = post.id;
    const title = post.title;
    const createDate = post.createDate;
    const tags = post.tags;
    const [username, setUsername] = useState("");
    const authorId = (post.author ? post.author.id : undefined);
    const [showModal, setShowModal] = useState(false);
    const navigate = useNavigate();

    const config = {
        headers: {
            Authorization: `Bearer ${me}`
        },
    }

    useEffect(() => {
        if (authorId !== undefined) {
            const endpoint = BASE_URL + `users/search?id=${authorId}`;
            axios.get(endpoint)
                .then((res) => {
                    setUsername(res.data.username);
                })
                .catch((err) => {
                    console.error("Cannot retrieve author", err)
                })
        } else {
            setUsername(undefined);
        }
    }, [authorId])


    const handleDeletePost = () => {
        setShowModal(true);
    }

    const confirmDelete = async() => {
        await deletePost(postId, config);
        (postsEndpoint!==undefined && setPosts!==undefined 
            ? axios.get(postsEndpoint)
                .then((res) => {
                    setPosts(res.data);
                    console.log("Post fetched");
                    setShowModal(false);
                })
                .catch((err) => {
                    console.error(err);
                })
            
            : navigate("/")
        )
    };
        
    const cancelDelete = () => {
        setShowModal(false);
    };

    const handleEditPost = () => {
        navigate(`/post/${postId}/edit`);
    }

    return (
    <div className={`post-card ${showDelete ? '' : 'hover-scale'}`} onDoubleClick={() => navigate(`/post/${postId}`)}>
        <div className="post-header">
            <h2 className="post-title">{title}</h2>
            <div className="icon-group">
                {showEdit
                    ? <FaEdit className="icon edit-icon" onClick={handleEditPost}/>
                    : <></>
                } 
                {showDelete 
                    ? <FaTimes className="icon delete-icon" onClick={handleDeletePost} /> 
                    : <></>
                }  
            </div>
        </div>
        <div className="tags-group">
            {tags 
                ?  
                    tags.map(tag => (
                        <Link 
                            key={tag.id} 
                            className="single-tag" 
                            style={{ textDecoration: 'none', color: 'blue' }}
                            onMouseEnter={(e) => e.target.style.textDecoration = 'underline'}
                            onMouseLeave={(e) => e.target.style.textDecoration = 'none'}
                            to={`/post/tag/${tag.name}`}
                        >
                            {`#${tag.name} `}  
                        </Link>
                    ))
                : <></>
            }
        </div>
        <div className="post-header">
            <p className="post-author">Author: 
                {(username !== undefined) 
                ? <Link 
                    className="author-link" 
                    style={{ textDecoration: 'none', color: 'blue' }}
                    onMouseEnter={(e) => e.target.style.textDecoration = 'underline'}
                    onMouseLeave={(e) => e.target.style.textDecoration = 'none'}                    
                    to={`/user/${username}`}
                    >
                        {username}        
                    </Link>
                : <span className="author-link">Deleted User</span>
                }
            </p>
            <p className="post-date">Created Date: {formattedDate(createDate)}</p>
        </div>
        {showModal && (
                <ConfirmationModal 
                    message={`Are you sure you want to delete this post? (${title})`}
                    onConfirm={confirmDelete}
                    onCancel={cancelDelete}/>
            )}
    </div>
    )
}

export default PostCard
