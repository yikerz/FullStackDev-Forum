import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom"
import { BASE_URL } from "../helper/connection";
import Posts from "../components/Posts";
import { Typography } from "@mui/material"
import { useNavigate } from "react-router-dom";


const TagPage = (props) => {
    const [me, setMe] = props.me;
    const { tagname } = useParams();
    const endpoint = BASE_URL + `posts/search?tag=${tagname}`
    const [posts, setPosts] = useState([]);
    const [errorMsg, setErrorMsg] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(endpoint)
            .then(res => {
                setPosts(res.data);
                console.log("Retrieve all posts with tag" + tagname);
            })
            .catch(err => {
                if (err.response && err.response.status === 404){
                    console.error(err)
                    navigate("/error");
                } else {
                    setErrorMsg("Fail to retrieve posts")
                    console.error(err) 
                } 
            })
    }, [me])

    return (
        <div className="content-wrapper">
            <h1>Posts with Tag: #{tagname}</h1>
            <Posts 
                me={[me, setMe]} 
                posts={[posts, setPosts]}
                endpoint={endpoint}
            />
            <Typography style={{color: "red"}}>{errorMsg}</Typography>
        </div>
    )
}

export default TagPage
