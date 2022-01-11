import React, {useState, useEffect} from 'react';
import {useParams, Route, Navigate, Routes} from 'react-router-dom'
import LeafletMap from "../../components/LeafletMap/LeafletMap";
import HistoryCharts from "./HistoryCharts/HistoryCharts"
import './CityView.css'
import PredictionCharts from "./PredictionCharts/PredictionCharts";
import NavBar from "../../components/NavBar/NavBar";

function CityView(props) {
    const [ready, setReady] = useState(false)
    const [center, setCenter] = useState()
    const [zoom, setZoom] = useState()
    const [geoJson, setGeoJson] = useState()
    const params = useParams()
    const {city} = params

    useEffect(() => {

        async function fetchData() {
            const cityAttrResponse = await fetch(`/api/cityview/${city}`)
            const cityLocationData = await cityAttrResponse.json()

            const geoResponse = await fetch(`/static/data/${city}-neighborhoods.geojson`)
            const geoJson = await geoResponse.json()

            // const covidHistoryResponse = await fetch(`/api/history/covid/${city}`)
            // const covidHistoryData = await covidHistoryResponse.json()
            // console.log("Return date: ", covidHistoryData['covidHistoryDate'])
            // console.log("Return data: ", covidHistoryData['covidHistoryData'])

            const {center, zoom} = cityLocationData
            setCenter(center)
            setZoom(zoom)
            setGeoJson(geoJson)
            setReady(true)
        }

        fetchData().catch(console.err)
    }, [city]);

    return (
        <>
            <div className="geovisual">
                {ready ? <LeafletMap center={center} zoom={zoom} geoJson={geoJson}/> :
                    <></>}
            </div>
            {/*<div className="switchtabs">*/}
            {/*    <NavLink className="historytab" to={`/cityview/${city}/history`}>History</NavLink>*/}
            {/*    <NavLink className="predictiontab" to={`/cityview/${city}/prediction`}>Prediction</NavLink>*/}
            {/*</div>*/}
            <div className="rightpane">
                <div className="tabs">
                    <NavBar city={city}/>
                </div>
                <div className="chartdisplay">
                    <Routes>
                        <Route path="history" element={<HistoryCharts/>}/>
                        <Route path="prediction" element={<PredictionCharts/>}/>
                        <Route path="*" element={<Navigate replace to={`/cityview/${city}/history`}/>}/>
                    </Routes>
                </div>
            </div>
        </>
    );
}

export default CityView;