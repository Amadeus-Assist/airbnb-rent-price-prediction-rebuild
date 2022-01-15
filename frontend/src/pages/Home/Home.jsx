import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom'

function Home() {
    const [cityInput, setCityInput] = useState('')
    const navigate = useNavigate();

    const saveValue = (event) => {
        setCityInput(event.target.value);
    }

    const doSearch = async () => {
        if (!(typeof cityInput == "undefined" || cityInput == null || cityInput === '')) {
            const response = await fetch(`/api/getSimilarCity/${cityInput}`);
            const data = await response.json();
            const {city} = data;
            navigate(`/cityview/${city}`)
        }
    }

    const handleKeyUp = (event) => {
        if (event.key === 'Enter') {
            doSearch().then()
        }
    }

    return (
        <div className="ui vertical masthead center aligned segment">
            <div className="ui text container">
                <h1 className="ui header">Airbnb Rent Price Prediction</h1>
                <h2>Input city names to navigate!</h2>
                <br/>
                <div className="ui fluid action input">
                    <input onChange={saveValue} onKeyUp={handleKeyUp} type="text" id="cityName"
                           placeholder="Search..."/>
                    <button onClick={doSearch} className="ui primary button" id="viewCity">View Maps</button>
                </div>
            </div>
        </div>
    );
}

export default Home;