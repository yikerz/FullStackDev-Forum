const formattedDate = (instant) => {
    const date = new Date(instant);

    const formattedDate = date.toLocaleString('en-US', {
        year: 'numeric', 
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
        timeZoneName: 'short',
    })

    return formattedDate;
}

export default formattedDate
