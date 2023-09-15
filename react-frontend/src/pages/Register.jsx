import { useState } from "react"
import UserForm from "../components/UserForm"
import { useNavigate } from "react-router-dom";
import { BASE_URL } from "../helper/connection";
import axios from "axios";

const Register = (props) => {
    const [me, setMe] = props.me;
    const [username, setUsername] = useState("");  
    const [password, setPassword] = useState(""); 
    const [errorMsg, setErrorMsg] = useState("");
    const navigate = useNavigate();

    const tryRegister = () => {
        const endpoint = BASE_URL + "users/register";
        const newUser = {
            "username": username,
            "password": password,
            "createDate": new Date().toISOString()
        }
        axios.post(endpoint, newUser)
            .then((res) => {
                console.log(`User (${username}) is registered`);
                navigate("/login");
            })
            .catch((err) => {
				if (err.response && err.response.status === 400) {
					setErrorMsg("Username already exists")
				} else {
					setErrorMsg("Something went wrong. Please check your internet connection or try again later")
				}
			})
    }

    return (
    <UserForm 
        title={"Register"} 
        ids={["username", "password"]} 
        firstState={[username, setUsername]} 
        secondState={[password, setPassword]}
        types={["search", "password"]}
        errorMsg={errorMsg} 
        onClick={tryRegister}
    />
    )
}

export default Register
