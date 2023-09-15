import { BASE_URL } from "../helper/connection";
import useTokenGetUser from "../helper/useTokenGetUser"
import useAxios from "../helper/useAxios"
import "../styles/UserCard.css"
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import ConfirmationModal from "./ConfirmationModal";
import { deleteUser } from "../helper/deleteFunctions";

const UserCard = (props) => {
    const [me, setMe] = props.me;
    const [targetUser, setTargetUser] = props.targetUser;
    const [posts, setPosts] = props.posts;
    const [myUser, setMyUser] = useTokenGetUser(me);
    const commentsEndpoint = BASE_URL + `users/articles?type=comments&username=${targetUser.username}`;
    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false);


    const [comments, setComments] = useAxios([], commentsEndpoint, () => {
        console.error("Fail to retrieve comments for user: " + targetUser.username);
    })

    if (!targetUser) {
        return <p>Loading user data...</p>; 
      }

    const changePassword = () => {
        navigate(`/update_password/${targetUser.username}`);
    }

    const userHandleDeleteAccount = () => {
        navigate(`/delete_user/${targetUser.username}`);
    }

    const adminHandleDeleteUser = () => {
        setShowModal(true);
    }

    const confirmDelete = async() => {
        const config = {
                headers: {
                    Authorization: `Bearer ${me}`
                },
            }
        await deleteUser(targetUser.username, config, undefined);
        navigate("/");
    };
        
    const cancelDelete = () => {
        setShowModal(false);
    };
    
    return (
        <div className="user-card">
            <h2 className="username">Username: {targetUser.username}</h2>
            <p className="create-date"> Create Date: {targetUser.createDate}</p>
            <p className="post-count">Number of posts: {posts.length}</p>
            <p className="comment-count">Number of comments: {comments.length}</p>
            {(myUser.username === targetUser.username) 
                ? <button className="btn" onClick={changePassword}>Change Password</button>
                : <></>}
            {(myUser.username === targetUser.username  && targetUser.username!=="root") 
                ? <button className="btn delete-btn" onClick={userHandleDeleteAccount}>Delete Account</button>
                : <></>}
            {(myUser.role === "ADMIN" && targetUser.username!=="root") 
            ? <button className="btn delete-btn" onClick={adminHandleDeleteUser}>Delete Account</button>
            : <></>}
            {showModal && (
                <ConfirmationModal 
                    message={`Are you sure you want to delete user: ${targetUser.username}?`}
                    onConfirm={confirmDelete}
                    onCancel={cancelDelete}/>
            )}
        </div>
    )
}

export default UserCard
