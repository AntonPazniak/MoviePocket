import './Navlist.css'
import {Link} from "react-router-dom";
import {useContext} from "react";
import {AuthContext} from "../../App";

const Navlist = () => {
    const isLoggedIn = useContext(AuthContext);
    return (
        <ul className="Navlist">
            <div className="Search">
                <input type="text" className="Round"/>
                <input type="image" className="Submit"
                       src="https://raw.githubusercontent.com/prymakD/MoviePocket/1458e6c307d0ae5d381bd8607aa7758ccef1a575/src/main/frontend/src/images/search.png"
                       alt="+"/>
            </div>
            {!isLoggedIn
                &&
                (<li className="NavListItem"><Link to="/login" className="NavListLink">SIGN IN</Link></li>)
            }
            {!isLoggedIn
                &&
                (<li className="NavListItem"><Link to="/registration" className="NavListLink">CREATE ACCOUNT</Link></li>)
            }
            <li className="NavListItem"><Link to="/films/1" className="NavListLink">FILMS</Link></li>
            <li className="NavListItem"><Link to="#" className="NavListLink">COMMUNITY</Link></li>
        </ul>
    )
}

export default Navlist;