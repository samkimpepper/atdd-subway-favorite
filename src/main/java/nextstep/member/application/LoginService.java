package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.message.Message;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_EXIST_EAMIL));
        if (!member.arePasswordsSame(tokenRequest.getPassword())) {
            throw new IllegalArgumentException(Message.INVALID_PASSWORD);
        }
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

}
