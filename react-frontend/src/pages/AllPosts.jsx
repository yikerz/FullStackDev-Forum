import Posts from "../components/Posts"
import { Button } from "@mui/material"
import { useNavigate } from "react-router-dom";
import useTokenGetUser from "../helper/useTokenGetUser";
import { BASE_URL } from "../helper/connection";
import useAxios from "../helper/useAxios"
import { Typography } from "@mui/material"
import { useState } from "react";


const AllPosts = (props) => {
  const [me, setMe] = props.me;
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();
  const endpoint = BASE_URL + "posts";
  const [posts, setPosts] = useAxios([], endpoint, () => {
    setErrorMsg("Fail to retrieve posts")
  } )

  return (
    <div className="content-wrapper">
      <h1>Posts</h1>
      {(!me) ? <></> : <Button variant="outlined" onClick={() => navigate("/post/create")}>Create Post</Button>}
      <Posts 
        me={[me, setMe]} 
        posts={[posts, setPosts]}
        endpoint={endpoint}
      />
      <Typography style={{color: "red"}}>{errorMsg}</Typography>
    </div>
  )
}

export default AllPosts
