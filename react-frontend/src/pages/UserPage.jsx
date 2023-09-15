import { useEffect, useState } from "react";
import useTokenGetUser from "../helper/useTokenGetUser"
import Posts from "../components/Posts"
import UserCard from "../components/UserCard";
import { useNavigate, useParams } from "react-router-dom";
import { BASE_URL } from "../helper/connection";
import axios from "axios";
import useAxios from "../helper/useAxios"
import { Typography } from "@mui/material"



const UserPage = (props) => {
    const [me, setMe] = props.me;
    const { targetName } = useParams();
    const [targetUser, setTargetUser] = useState({});
    const [errorMsg, setErrorMsg] = useState("");
    const postsEndpoint = BASE_URL 
                    + `users/articles?type=posts&username=${targetName}`
    const [posts, setPosts] = useAxios([], postsEndpoint, () => {
        setErrorMsg("Fail to retrieve posts")
    } )
    const navigate = useNavigate();


    useEffect(() => {
        const endpoint = BASE_URL + `users/search?username=${targetName}`
        axios.get(endpoint)
            .then((res) => {
                setTargetUser(res.data);
            })
            .catch(err => {
                console.error(err);
                navigate("/error");
            })
    }, [targetName])

    return (
    <div className="content-wrapper">
        <h1>Account Details</h1>
        <div className="user-card-container">
            <UserCard 
                me={[me, setMe]}
                targetUser={[targetUser, setTargetUser]} 
                posts={[posts, setPosts]}
            />
        </div>
        <h3>Post History</h3>
        <Posts 
            me={[me, setMe]} 
            targetUser={targetUser} 
            endpoint={postsEndpoint}
            posts={[posts, setPosts]}
        />
        <Typography style={{color: "red"}}>{errorMsg}</Typography>
    </div>
    )
}

export default UserPage
