import axios from "axios";
import { useEffect, useState } from "react"

const useAxios = (defaultValue, endpoint, errorCallback) => {
    const [data, setData] = useState(defaultValue);
    useEffect(() => {
        axios.get(endpoint)
            .then((res) => {
                setData(res.data)
            })
            .catch((err) => {
                if (errorCallback != undefined) {
                    errorCallback(err);
                } else {
                    console.error(err);
                }
            })
    }, [endpoint])

  return (
    [data, setData]
  )
}

export default useAxios
