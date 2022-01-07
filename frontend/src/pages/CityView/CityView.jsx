import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router-dom'
import Charts from "../../components/Display/Charts/Charts";
import LeafletMap from "../../components/LeafletMap/LeafletMap";

function CityView(props) {
    const [ready, setReady] = useState(false)
    const [center, setCenter] = useState()
    const [zoom, setZoom] = useState()
    const [geoJson, setGeoJson] = useState()
    const params = useParams()
    console.log("useParams: ", params)
    const {city} = params

    useEffect(() => {

        async function fetchData() {
            const geoResponse = await fetch(`/static/data/${city}-neighborhoods.geojson`)
            const geoJson = await geoResponse.json()

            const cityAttrResponse = await fetch(`/api/cityview/${city}`)
            const cityLocationData = await cityAttrResponse.json()
            console.log("cityLocData: ", cityLocationData)

            const {center, zoom} = cityLocationData
            setCenter(center)
            setZoom(zoom)
            setGeoJson(geoJson)
            setReady(true)
        }

        fetchData().catch(console.err)
    }, [city]);

    return (
        <div>
            <div className="geovisual">
                {ready ? <LeafletMap center={center} zoom={zoom} geoJson={geoJson}/> :
                    <></>}
            </div>
            <Charts/>
        </div>
    );
}

export default CityView;