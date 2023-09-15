import { useState } from "react"
import { BASE_URL } from "../helper/connection" 
import axios from "axios"
import { useNavigate } from "react-router-dom"
import UserForm from "../components/UserForm"

const Login = (props) => {
	const [me, setMe] = props.me;
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [errorMsg, setErrorMsg] = useState("");
	const navigate = useNavigate();

	const tryLogin = () => {
		const endpoint = BASE_URL + "auth/login";
		const config = {
			auth: {
				username: username,
				password: password
			}
		}
		axios.post(endpoint, {}, config)
			.then((res) => {
				setMe(res.data);
				localStorage.setItem("me", res.data);
				console.log(`${username} login successfully`);
				navigate("/");
			})
			.catch((err) => {
				if (err.response && err.response.status === 401) {
					setErrorMsg("Invalid username or password")
				} else {
					setErrorMsg("Something went wrong. Please check your internet connection or try again later")
				}
			})
	}

	return (
		<UserForm 
			title={"Login"} 
			ids={["username", "password"]} 
			firstState={[username, setUsername]} 
			secondState={[password, setPassword]}
			types={["search", "password"]}
			errorMsg={errorMsg} 
			onClick={tryLogin}
		/>
	);
}

export default Login
