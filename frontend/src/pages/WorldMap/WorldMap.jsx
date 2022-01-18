import React, {useEffect, useState} from 'react';
import LeafletMap from "../../components/LeafletMap/LeafletMap";

function WorldMap() {
    const [ready, setReady] = useState(false)
    const [markers, setMarkers] = useState()

    useEffect(() => {
        async function fetchData() {
            const markersResponse = await fetch('/api/markers')
            const markersData = await markersResponse.json()

            setMarkers(markersData.markers)
            setReady(true)
        }

        fetchData().catch(console.err)
    }, [])

    return (
        ready ? <LeafletMap center={[40, -95]} zoom={3.5} markers={markers}/> : <></>
    );
}

export default WorldMap;