import UnAuthorization from "pages/Unauthorization/UnAuthorization";
import React from "react";
class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false, errorCoode: "" };
    }

    static getDerivedStateFromError(error) {
        console.log(error);
        if(error == "401" || error.code === "ERR_BAD_REQUEST") {
            console.log(typeof error);
            return { hasError: true, errorCode: "401" };
        }
        
    }
    


    render() {
        if (this.state.hasError && this.state.errorCode === "401") {
            // 폴백 UI를 커스텀하여 렌더링할 수 있습니다.
            return (
                <UnAuthorization />
            );
            // <Navigate to="/unauthorization" replace={true} />;
        }
        return this.props.children;
    }
}

export default ErrorBoundary;