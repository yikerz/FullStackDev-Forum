import { useEffect, useState } from "react"
import { BASE_URL } from "./connection";
import axios from "axios";

const useTokenGetUser = (me) => {
    const [user, setUser] = useState({});
    const endpoint = BASE_URL + "users/auth"
    const config = {
        headers: {
            Authorization: `Bearer ${me}`
        },
    }
    
    useEffect(() => {
        if(me){
            axios.get(endpoint, config)
                .then((res) => {
                    setUser(res.data);
                    console.log(`Get user (${res.data.username}) from bearer token`);
                })
                .catch((err) => {
                    console.log("Fail to get user from bearer token");
                })
        }
    }, [me])

    return (
        [user, setUser]
    )
}

export default useTokenGetUser

