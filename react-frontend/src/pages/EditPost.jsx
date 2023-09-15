import { useParams } from "react-router-dom";
import { BASE_URL } from "../helper/connection";
import { useEffect, useState } from "react";
import axios from "axios";
import PostForm from "../components/PostForm";
import { useNavigate } from "react-router-dom";



const EditPost = (props) => {
    const [me, setMe] = props.me;
    const { postId } = useParams();
    const endpoint = BASE_URL + "posts/search?id=" + postId;
    const [errorMsg, setErrorMsg] = useState("");
    const [post, setPost] = useState([]);
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [tags, setTags] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(endpoint)
            .then((res) => {
                setPost(res.data[0])
                console.log("Retrieved Post")
                setTitle(res.data[0].title)
                setContent(res.data[0].content)
                setTags(res.data[0].tags)
            })
            .catch((err) => {
                setErrorMsg()
            })
    }, []);

    const handleSubmit = () => {
        if (title.trim() !=="" ) {
            const updateEndPoint = BASE_URL + `posts/${postId}/update`
            const updatedPost = {
                "title": title,
                "content": content,
                "tags": tags
            }
            const config = {
                headers: {
                    Authorization: `Bearer ${me}`
                },
            }


            axios.put(updateEndPoint, updatedPost, config)
                .then((res) => {
                    console.log("Updated Post");
                    navigate(`/post/${postId}`);
                })
                .catch((err) => {
                    setErrorMsg("Something went wrong.")
                })
        } else {
            alert("Title cannot be empty")
        } 
    }

    return (
        <PostForm 
            header="Edit Post"
            title={[title, setTitle]}
            content={[content, setContent]}
            tags={[tags, setTags]}
            errorMsg={[errorMsg, setErrorMsg]}
            handleSubmit={handleSubmit}
        />
    )
}

export default EditPost
