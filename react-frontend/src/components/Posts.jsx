import { useEffect, useState } from "react";
import { BASE_URL } from "../helper/connection";
import { Typography } from "@mui/material"
import PostCard from "./PostCard";
import axios from "axios";


const Posts = (props) => {
    const [me, setMe] = props.me
    const targetUser = props.targetUser;
    const endpoint = props.endpoint;
    const [posts, setPosts] = props.posts;
    const [showDelete, setShowDelete] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");

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
                    (res.data.role === "ADMIN" || (targetUser && res.data.username === targetUser.username) ? setShowDelete(true) : setShowDelete(false))
                    console.log(`Get user (${res.data.username}) from bearer token`);
                    (targetUser && res.data.username === targetUser.username ? setShowEdit(true) : setShowEdit(false))
                })
                .catch((err) => {
                    console.log(err);
                })
        } else {
            setShowDelete(false);
            setShowEdit(false)
        }
    }, [me, targetUser]) 

    return (
    <div>
        {posts.map((post) => (
        <PostCard 
                key={post.id}
                post={post}
                showIcons={[showDelete, showEdit]}
                me={[me, setMe]}
                fetchPosts={[endpoint, setPosts]}
        />
        ))}
        <Typography style={{color: "red"}}>{errorMsg}</Typography>
    </div>
    )
}

export default Posts
