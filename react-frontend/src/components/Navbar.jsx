import { Link, useNavigate } from "react-router-dom"
import useTokenGetUser from "../helper/useTokenGetUser"
import "../styles/Navbar.css"
import { useEffect, useState } from "react";
import { BASE_URL } from "../helper/connection";

const Navbar = (props) => {
  const [me, setMe] = props.me;
  const [user, setUser] = useTokenGetUser(me);
  const navigate = useNavigate();
  const [searchBy, setSearchBy] = useState("author");
  const [searchValue, setSearchValue] = useState("");

  const tryLogout = () => {
    setMe("");
    localStorage.removeItem("me");
    console.log("Logout successfully");
    navigate("/");
  }

  const handleSearch = () => {
    if (searchBy === "tag") {
      const endpoint = `/post/tag/${searchValue}`
      navigate(endpoint);
    } else if (searchBy === "author") {
      const endpoint = `/user/${searchValue}`
      navigate(endpoint);
    }
    setSearchBy("author");
    setSearchValue("");
  }

  return (
    <nav className="navbar">
      <ul className="nav-left">
        <Link className="nav-item" to="/">All Posts</Link>
      </ul>
      <ul className="nav-center">
        <div className="search-bar">
          <select
            value={searchBy}
            onChange={(e) => setSearchBy(e.target.value)}
            className="search-dropdown"
          >
            <option value="author">Author</option>
            <option value="tag">Tag</option>
          </select>
          <input
            type="text"
            className="search-input"
            value={searchValue}
            placeholder={`Search by ${searchBy}...`}
            onChange={(e) => setSearchValue(e.target.value)}
          />
          <button className="search-button" onClick={() => handleSearch()}>
            Search
          </button>
        </div>
      </ul>
      <ul className="nav-right">
        {(!me) 
          ? <>
              <Link className="nav-item" to="/login">Sign In</Link>
              <Link className="nav-item register" to="/register">Sign Up</Link>
            </>
          : <>
              <Link className="nav-item" to={"/user/" + user.username}>My Account</Link>
              <Link className="nav-item logout" to="/" onClick={tryLogout}>Logout</Link>
            </>
        }
      </ul>
    </nav>
  );
}

export default Navbar
