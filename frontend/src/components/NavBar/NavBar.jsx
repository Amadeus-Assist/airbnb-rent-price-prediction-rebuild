import React from 'react';
import {NavLink} from 'react-router-dom';
import './NavBar.css'

function NavBar(props) {
    const {city} = props

    return (
        <ul className="nav nav-tabs nav-custom">
            <li className="nav-item nav-item-custom">
                <NavLink className="nav-link nav-link-custom" activeClassName="active" to={`/cityview/${city}/history`}>History</NavLink>
            </li>
            <li className="nav-item">
                <NavLink className="nav-link" to={`/cityview/${city}/prediction`}>Prediction</NavLink>
            </li>
        </ul>
    );
}

export default NavBar;