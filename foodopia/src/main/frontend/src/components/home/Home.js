import React from 'react';
import { Container, Row, Col, Card, Button, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Home = () => {
    const { currentUser, isAuthenticated } = useAuth();

    return (
        <div>
            <div className="hero-section">
                <h1>Welcome to Foodopia</h1>
                <p>Delicious food delivered to your doorstep</p>
                {!isAuthenticated && (
                    <Button as={Link} to="/register" variant="light" size="lg" className="mt-3">
                        Get Started
                    </Button>
                )}
            </div>

            {isAuthenticated && (
                <Alert variant="success" className="text-center mb-4">
                    Welcome back, <strong>{currentUser.username}</strong>! Ready to order some delicious food?
                </Alert>
            )}

            <Row className="mb-4">
                <Col md={4} className="mb-3">
                    <Card className="h-100">
                        <Card.Img variant="top" src="https://source.unsplash.com/random/300x200/?pizza" />
                        <Card.Body>
                            <Card.Title>Fast Delivery</Card.Title>
                            <Card.Text>
                                We deliver your food as fast as possible. Our delivery is always on time.
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4} className="mb-3">
                    <Card className="h-100">
                        <Card.Img variant="top" src="https://source.unsplash.com/random/300x200/?restaurant" />
                        <Card.Body>
                            <Card.Title>Quality Food</Card.Title>
                            <Card.Text>
                                All our meals are prepared with high-quality ingredients by professional chefs.
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4} className="mb-3">
                    <Card className="h-100">
                        <Card.Img variant="top" src="https://source.unsplash.com/random/300x200/?chef" />
                        <Card.Body>
                            <Card.Title>Best Offers</Card.Title>
                            <Card.Text>
                                We provide the best deals and offers to make your dining experience affordable.
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <div className="text-center my-5">
                <h2>Ready to Order?</h2>
                <p className="lead">Browse our menu and find your favorite dishes</p>
                <Button as={Link} to="/menu" variant="primary" size="lg" className="mt-3">
                    View Menu
                </Button>
            </div>
        </div>
    );
};

export default Home;