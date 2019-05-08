import React from "react";
//id, author, title, text, creationDateTime, tags, voteCount
        const UserSummary = ({ user, onViewDetails }) => (
<div className="box content">
<article className="post">
<h4>
<a onClick={() => onViewDetails(user.id)}>{user.title}</a>
</h4>
<div className="media">
<div className="media-left">
        {user.creationDateTime}
</div>
<div className="media-content">
<div className="content">
<p>
                            {user.author.username} score: {user.author.score} &nbsp;
                                    {
                                    user.tags.map((tag, index) => (
<span><span className="tag">{tag} </span> &nbsp;</span>
        ))
        }
</p>
</div>
</div>
<div className="media-right">
<span className="has-text-grey-light">{user.voteCount}</span>&nbsp;
</div>
</div>
</article>
</div>


        );

        export default UserSummary;